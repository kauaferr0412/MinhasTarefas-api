package com.kauaferr.minhasTarefas.controllers;

import com.kauaferr.minhasTarefas.DTO.ProdutoDTO;
import com.kauaferr.minhasTarefas.model.entity.Produto;
import com.kauaferr.minhasTarefas.model.repository.ProdutoRepository;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.*;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoRepository produtoRepository;

    @Value("${mercadopago.access.token}")
    private String mercadoPagoAccessToken;

    public ProdutoController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @PostMapping
    public ResponseEntity<ProdutoDTO> criarProduto(@RequestBody ProdutoDTO produtoDTO) {
        Produto produto = Produto.builder()
                .title(produtoDTO.getTitle())
                .description(produtoDTO.getDescription())
                .categoryId(produtoDTO.getCategoryId())
                .quantity(produtoDTO.getQuantity())
                .currencyId(produtoDTO.getCurrencyId())
                .unitPrice(produtoDTO.getUnitPrice())
                .pictureUrl(produtoDTO.getPictureUrl())
                .build();

        Produto produtoSalvo = produtoRepository.save(produto);

        return ResponseEntity.ok(ProdutoDTO.builder()
                .id(produtoSalvo.getId())
                .title(produtoSalvo.getTitle())
                .description(produtoSalvo.getDescription())
                .categoryId(produtoSalvo.getCategoryId())
                .quantity(produtoSalvo.getQuantity())
                .currencyId(produtoSalvo.getCurrencyId())
                .unitPrice(produtoSalvo.getUnitPrice())
                .pictureUrl(produtoSalvo.getPictureUrl())
                .build());
    }

    @PostMapping("/{id}/pagamento")
    public ResponseEntity<?> criarPagamento(@PathVariable Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        try {
            MercadoPagoConfig.setAccessToken(mercadoPagoAccessToken);
            PreferenceClient client = new PreferenceClient();

            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .id(produto.getId().toString())
                    .title(produto.getTitle())
                    .description(produto.getDescription())
                    .pictureUrl(produto.getPictureUrl())
                    .categoryId(produto.getCategoryId())
                    .quantity(produto.getQuantity())
                    .currencyId(produto.getCurrencyId())
                    .unitPrice(produto.getUnitPrice())
                    .build();

            List<PreferenceItemRequest> items = new ArrayList<>();
            items.add(itemRequest);

            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items)
                    .backUrls(
                            PreferenceBackUrlsRequest.builder()
                                    .success("https://seusite.com/success")
                                    .failure("https://seusite.com/failure")
                                    .pending("https://seusite.com/pending")
                                    .build())
                    .autoReturn("approved")
                    .binaryMode(true)
                    .build();


            Preference preference = client.create(preferenceRequest);

            return ResponseEntity.ok(preference);

        } catch (MPApiException apiException) {
            System.err.println(apiException.getApiResponse().getContent());
            return ResponseEntity.status(apiException.getStatusCode())
                    .body(apiException.getApiResponse().getContent());
        } catch (MPException ex) {
            System.err.println(ex.getMessage());
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }
}