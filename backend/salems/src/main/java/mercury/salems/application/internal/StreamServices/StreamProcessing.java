package mercury.salems.application.internal.StreamServices;

import java.util.function.Function;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import mercury.shareDomain.Order;
import mercury.shareDomain.ProductSchema; 

@Configuration
public class StreamProcessing {
    public final static String PRODUCT_SALE = "product_sale";
    public final static String ORDER_SALE = "order_sale";
    
/*    @Bean
    public Function<KStream<?, ProductSchema>, KStream<String, Order>> process(){
        return inputStream -> {
            inputStream.flatMapValues(value -> Arrays.asList(value))
                .map((Key, value) -> new KeyValue<>(value, value))
                .groupBy(Grouped.with(Serdes.String(), Serdes.String()))
                .count(Materialized.as(PRODUCT_SALE))
                .toStream()
                .map((key, value) -> new KeyValue<>(key.key(), new Order(key.key(), value, new Date(window().start()), new Date(key.window().end()))));
        };
    } */

    @Bean
    public Function<KStream<?, ProductSchema>, KStream<String, Order>> process(){
        return inputStream -> {

            inputStream.map((key, value) -> {
                String productID = value.getProductName() + value.getId();
                ProductSchema tempProduct = new ProductSchema();
                return KeyValue.pair(productID, tempProduct);
            }).toTable(
                Materialized.<String, ProductSchema, KeyValueStore<Bytes, byte[]>> as (PRODUCT_SALE)
                        .withKeySerde(Serdes.String())
            );

            KTable<String, Order> productSaleTable = inputStream.mapValues(ProductSchema::getProductName).
                                                                groupBy((keyIgnored, value) -> value).
                                                                count(
                                                                    Materialized.<String, Order, KeyValueStore<Bytes, byte[] >> as (ORDER_SALE).
                                                                    withKeySerde(Serdes.String()).
                                                                    withValueSerde(value.getQuantity())
                                                                );

            KStream<String, Order> productCountStream = productSaleTable.toStream().
                                                                            map((key,value) -> 
                                                                                KeyValue.pair((String) value.getProductName(), value.getQuantity())
                                                                                .groupByKey(Grouped.with(Serdes.String(), Serdes.Integer()))
                                                                                .reduce(Integer::sum));
            productCountStream.print(Printed.<String, Order>toSysOut().withLabel("Console Output: "));
            return productCountStream;
        };
    }
}