package com.sreejesh.demo.route;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cache.CacheConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="camel-demo-route")
@Data
@EqualsAndHashCode(callSuper=true)

public class CamelCacheRoute extends RouteBuilder {


	
	@Override
	public void configure() {

		// @formatter:off



		from("timer://cacheProducerTimer?period=2s&repeatCount=5")
		.routeId("CamelCacheProducerRoute")
		.log("*** CamelCacheProducerRoute Started-CamelTimerCounter:${exchangeProperty.CamelTimerCounter}  ***")
		.setHeader(CacheConstants.CACHE_OPERATION, constant(CacheConstants.CACHE_OPERATION_ADD))
		.setHeader(CacheConstants.CACHE_KEY, simple("key-${exchangeProperty.CamelTimerCounter}"))
		.setBody(simple("value-${exchangeProperty.CamelTimerCounter}"))
		.to("cache://TestCache1")
		.log("*** Finished putting key! ***")
		;

		from("timer://myCacheValueRetrieverTimer?period=3s&repeatCount=5")
		.routeId("GetKeyCamelCacheRoute")
		.log("*** GetKeyCamelCacheRoute Started-CamelTimerCounter:${exchangeProperty.CamelTimerCounter}  ***")
		.setHeader(CacheConstants.CACHE_OPERATION, constant(CacheConstants.CACHE_OPERATION_GET))
		.setHeader(CacheConstants.CACHE_KEY, simple("key-${exchangeProperty.CamelTimerCounter}"))
		.to("cache://TestCache1")
		.log("*** Got value for key-${exchangeProperty.CamelTimerCounter}:${body}  ***")
		;

//		This is an event driven notification when a new key is put
/*		from("cache://TestCache1?maxElementsInMemory=1000" +
				"&memoryStoreEvictionPolicy=emoryStoreEvictionPolicy.LFU" +
				"&overflowToDisk=true&eternal=true&timeToLiveSeconds=300" +
				"&timeToIdleSeconds=10&diskPersistent=true&diskExpiryThreadIntervalSeconds=300")
		.log("blah:${body}")
		;*/


		// @formatter:on

	}



}
