package dsm.api.pi.Service;

import br.com.efi.efisdk.EfiPay;
import br.com.efi.efisdk.exceptions.EfiPayException;
import com.fasterxml.jackson.databind.util.JSONPObject;
import dsm.api.pi.Config.PixConfig;
import dsm.api.pi.DTO.Pix.PixRequestPayload;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class PixService {

    private final JSONObject configuracoes;

    public PixService(final PixConfig pixConfig){
        this.configuracoes = new JSONObject();
        this.configuracoes.put("client_id", pixConfig.clientId());
        this.configuracoes.put("client_secret", pixConfig.clientSecret());
        this.configuracoes.put("certificate", pixConfig.certificatePath());
        this.configuracoes.put("sandbox", pixConfig.sandbox());
        this.configuracoes.put("debug", pixConfig.debug());
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

    public JSONObject criarCobrancaServico(String nomeCliente, BigDecimal valor, String chave) {
        JSONObject body = new JSONObject();
        body.put("calendario", new JSONObject().put("expiracao", 3600));
        body.put("devedor", new JSONObject().put("nome", nomeCliente));
        body.put("valor", new JSONObject()
                .put("original", valor.setScale(2, RoundingMode.HALF_UP).toPlainString()));
        body.put("chave", chave);

        try {
            EfiPay efi = new EfiPay(configuracoes);
            return efi.call("pixCreateImmediateCharge", new HashMap<>(), body);
        } catch (EfiPayException e) {
            log.error("Erro da API {} {}", e.getErrorDescription(), e.getError());
        } catch (Exception e) {
            log.error("Erro genérico {}", e.getMessage());
        }
        return null;
    }

    public JSONObject gerarQrCodeImagem(String locId) {
        Map<String, String> params = new HashMap<>();
        params.put("id", locId);
        return executarOperacao("pixGenerateQRCode", params);
    }

    private JSONObject executarOperacao(String operacao, Map<String, String> params) {
        final var retorno = new JSONObject();
        try {
            EfiPay efi = new EfiPay(configuracoes);
            JSONObject response = efi.call(operacao, params, new JSONObject());
            log.info("Resultado: {}", response);
            return response;
        } catch (EfiPayException e) {
            log.error(e.getError());
            retorno.put("erro", e.getErrorDescription());
        } catch (UnsupportedOperationException | JSONException operationException) {
            log.warn("Invalid JSON format {}", operationException.getMessage());
        } catch (Exception e) {
            retorno.put("erro", "Não foi possível completar a operação!");
        }
        return retorno;
    }

}