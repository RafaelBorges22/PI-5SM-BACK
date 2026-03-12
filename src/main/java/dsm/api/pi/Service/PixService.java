package dsm.api.pi.Service;

import br.com.efi.efisdk.EfiPay;
import br.com.efi.efisdk.Endpoints;
import br.com.efi.efisdk.exceptions.EfiPayException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dsm.api.pi.Config.PixConfig;
import dsm.api.pi.DTO.Pix.PixRequestPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class PixService {

    private final PixConfig pixConfig;
    private EfiPay criarClienteEfi() throws Exception {

        Map<String, Object> options = new HashMap<>();

        options.put("client_id", pixConfig.clientId());
        options.put("client_secret", pixConfig.clientSecret());
        options.put("certificate", pixConfig.certificatePath());
        options.put("sandbox", pixConfig.sandbox());

        return new EfiPay(options);
    }

    public JSONObject criarCobrancaServico(String nomeCliente, BigDecimal valor, String chavePix) throws Exception {

        Map<String, Object> options = new HashMap<>();
        options.put("client_id", pixConfig.clientId());
        options.put("client_secret", pixConfig.clientSecret());
        options.put("certificate", pixConfig.certificatePath());
        options.put("sandbox", pixConfig.sandbox());

        Endpoints efi = new Endpoints(options);

        String valorFormatado = valor.setScale(2, RoundingMode.HALF_UP)
                .toPlainString();

        JSONObject body = new JSONObject();
        body.put("calendario", new JSONObject().put("expiracao", 3600));
        body.put("valor", new JSONObject().put("original", valorFormatado));
        body.put("chave", chavePix);
        body.put("solicitacaoPagador", "Servico Barbearia - " + nomeCliente);

        log.info(">>> Body enviado para Efí: {}", body);

        return efi.call("pixCreateImmediateCharge", new HashMap<>(), body);
    }

    public JSONObject listarChavesPix(){
        return executarOperacao("pixListEvp", new HashMap<>());
    }

    public JSONObject criarChavePix(){
        return executarOperacao("pixCreateEvp", new HashMap<>());
    }

    public JSONObject deletarChavePix(final String chavePix){
        Map<String, String> params = new HashMap<>();
        params.put("chave", chavePix);
        return executarOperacao("pixDeleteEvp", params);
    }

    public JSONObject gerarQrCodeImagem(String locId) throws Exception {

        Map<String, Object> options = new HashMap<>();
        options.put("client_id", pixConfig.clientId());
        options.put("client_secret", pixConfig.clientSecret());
        options.put("certificate", pixConfig.certificatePath());
        options.put("sandbox", pixConfig.sandbox());

        Endpoints efi = new Endpoints(options);

        Map<String, String> params = new HashMap<>();
        params.put("id", locId);

        Object resultado = efi.call("pixGenerateQRCode", params, new HashMap<>());

        log.info(">>> Tipo retornado: {} | Conteúdo: {}", resultado.getClass().getName(), resultado);

        if (resultado instanceof JSONObject json) {
            return json;
        }

        if (resultado instanceof Map<?, ?> map) {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(map);
            return new JSONObject(jsonString);
        }

        log.warn(">>> Tipo inesperado do SDK: {}", resultado.getClass());
        return null;
    }

    private JSONObject executarOperacao(String operacao, Map<String, String> params) {
        final var retorno = new JSONObject();
        try {
            EfiPay efi = criarClienteEfi();
            JSONObject response = efi.call(operacao, params, new JSONObject());
            log.info("Resultado: {}", response);
            return response;
        } catch (EfiPayException e) {
            log.error(e.getError());
            retorno.put("erro", e.getErrorDescription());
        } catch (UnsupportedOperationException | JSONException operationException) {
            log.warn("Invalid JSON format {}", operationException.getMessage());
        } catch (Exception e) {
            log.error("Erro inesperado na operação {}: {}", operacao, e.getMessage(), e);
            retorno.put("erro", "Não foi possível completar a operação!");        }
        return retorno;
    }

    public JSONObject criarQrCode(PixRequestPayload pixRequestPayload) {

        String valorFormatado = pixRequestPayload.valor()
                .setScale(2, RoundingMode.HALF_UP)
                .toPlainString();

        JSONObject body = new JSONObject();
        body.put("calendario", new JSONObject().put("expiracao", 3600));
        body.put("devedor", new JSONObject()
                .put("cpf", "12345678909")
                .put("nome", "Feltex Silva"));
        body.put("valor", new JSONObject().put("original", valorFormatado));
        body.put("chave", pixRequestPayload.chave());

        JSONArray infoAdicionais = new JSONArray();
        infoAdicionais.put(new JSONObject().put("nome", "Campo 1").put("valor", "Informação Adicional1"));
        infoAdicionais.put(new JSONObject().put("nome", "Campo 2").put("valor", "Informação Adicional2"));
        body.put("infoAdicionais", infoAdicionais);

        try {
            EfiPay efi = criarClienteEfi();
            JSONObject cobranca = efi.call("pixCreateImmediateCharge", new HashMap<>(), body);

            // Pega o locId e já busca o QR Code
            String locId = cobranca.getJSONObject("loc").get("id").toString();
            JSONObject qrCode = gerarQrCodeImagem(locId);

            // Retorna tudo junto
            JSONObject resposta = new JSONObject();
            resposta.put("txid", cobranca.getString("txid"));
            resposta.put("pixCopiaECola", cobranca.getString("pixCopiaECola"));
            resposta.put("status", cobranca.getString("status"));
            resposta.put("valor", valorFormatado);

            if (qrCode != null) {
                resposta.put("qrcode", qrCode.optString("qrcode"));
                resposta.put("imagemQrcode", qrCode.optString("imagemQrcode"));
            }

            return resposta;

        } catch (EfiPayException e) {
            log.error("Erro da API {} {}", e.getErrorDescription(), e.getError());
        } catch (Exception e) {
            log.error("Erro genérico {}", e.getMessage(), e);
        }
        return null;
    }


}