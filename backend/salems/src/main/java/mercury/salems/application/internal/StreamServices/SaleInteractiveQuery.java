package mercury.salems.application.internal.StreamServices;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.stereotype.Service;

@Service
public class SaleInteractiveQuery {
    
    private final InteractiveQueryService saleQueryService;

    public SaleInteractiveQuery(InteractiveQueryService saleQueryService){
        this.saleQueryService = saleQueryService;
    }

    public long getProductQuantity(String ID){
        if (productSale().get(ID) != null) {
            return productSale().get(ID);
        }
        else{
            throw new NoSuchElementException();
        }
    }

    public List<String> getProductSaleList(){
        List<String> saleList = new ArrayList<>();
        KeyValueIterator<String, Long> productList = productSale().all();
        while (productList.hasNext()){
            String next = productList.next().key;
            saleList.add(next);
        }
        return saleList;
    }

    private ReadOnlyKeyValueStore<String, Long> productSale() {
        return this.saleQueryService.getQueryableStore(StreamProcessing.PRODUCT_SALE,
                QueryableStoreTypes.keyValueStore());
    }
}
