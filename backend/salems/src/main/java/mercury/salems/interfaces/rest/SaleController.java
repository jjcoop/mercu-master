package mercury.salems.interfaces.rest;

import mercury.salems.application.internal.outboundservices.ProductLookupBySale;
import mercury.salems.application.internal.outboundservices.acl.ExternalGetProductBySale;
import mercury.salems.application.internal.commandservices.SaleCommandService;
import mercury.salems.application.internal.queryservices.SaleQueryService;
import mercury.salems.domain.aggregate.InStoreSale;
import mercury.salems.domain.aggregate.OnlineSale;
import mercury.salems.domain.aggregate.Sale;
import mercury.salems.domain.aggregate.Store;
import mercury.shareDomain.ProductSchema;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
public class SaleController {
    private final SaleCommandService commandService;
    private final SaleQueryService queryService;
    private final ProductLookupBySale productLookupBySale;
    @Autowired
    ExternalGetProductBySale getProductBySale;

    public SaleController(SaleCommandService commandService, SaleQueryService queryService, ProductLookupBySale productLookupBySale) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.productLookupBySale = productLookupBySale;
    }

    // **********************************************************************
    // SALES
    // **********************************************************************
    @GetMapping("/sales")
    public CollectionModel<EntityModel<Sale>> all() {
        return queryService.all();
    }

    @GetMapping("/sales/{id}")
    public Sale one(@PathVariable Long id) {
        return queryService.findById(id);
    }

    // **********************************************************************
    // UNAVAILABLE READY FOR BACKORDER
    // **********************************************************************
    @GetMapping("/sales/unavailable")
    public List<Sale> getUnavailable() {
        return queryService.getUnavailable();
    }

    // **********************************************************************
    // ONLINE SALES
    // **********************************************************************
    @PostMapping("/sales/online")
    ResponseEntity<?> addOnlineSale(@RequestBody OnlineSale sale) {
        return commandService.addOnlineSale(sale);
    }

    @GetMapping("/sales/online")
    public CollectionModel<EntityModel<OnlineSale>> allOnlineSales() {
        return queryService.allOnlineSales();
    }

    @GetMapping("/sales/online/{id}")
    public EntityModel<OnlineSale> oneOnlineSale(@PathVariable Long id) {
        return queryService.findOnlineSaleById(id);
    }

    // **********************************************************************
    // STORE
    // **********************************************************************
    @PostMapping("/sales/store")
    ResponseEntity<?> addStore(@RequestBody Store store) {
        return commandService.addStore(store);
    }

    @GetMapping("/sales/store")
    public CollectionModel<EntityModel<Store>> allStores() {
        return queryService.allStores();
    }

    @GetMapping("/sales/store/{id}")
    public Store getStore(@PathVariable Long id) {
        return queryService.findStoreById(id);
    }

    // **********************************************************************
    // STORE SALE
    // **********************************************************************
    @PostMapping("/sales/store/{storeId}")
    ResponseEntity<?> addInStoreSale(@PathVariable Long storeId, @RequestBody InStoreSale sale) {
        return commandService.addInStoreSale(storeId, sale);
    }

    @GetMapping("/sales/store/purchases")
    public CollectionModel<EntityModel<InStoreSale>> allInStoreSales() {
        return queryService.allInStoreSales();
    }

    @GetMapping("/sales/store/{storeId}/purchases")
    public Set<InStoreSale> oneStorePurchases(@PathVariable Long storeId) {
        return queryService.oneStorePurchases(storeId);
    }
    
    
    // **********************************************************************
    // STORE SALE
    // **********************************************************************
    @GetMapping("/sales/backorder/{id}")
    public Sale backorder(@PathVariable Long id) {
        return commandService.backorder(id);
    }


    // **********************************************************************
    // GET PRODUCT BY SALE
    // **********************************************************************
    @GetMapping("/sales/{id}/product")
    ProductSchema getProductBySale(@PathVariable Long id) {
        //return productLookupBySale.getProductBySale(id);
        return getProductBySale.getProductBySale(id);
    }
}
