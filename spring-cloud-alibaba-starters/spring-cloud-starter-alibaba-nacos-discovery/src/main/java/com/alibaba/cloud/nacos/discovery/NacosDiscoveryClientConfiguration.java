/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.cloud.nacos.discovery;

import com.alibaba.cloud.nacos.ConditionalOnNacosDiscoveryEnabled;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.CommonsClientAutoConfiguration;
import org.springframework.cloud.client.ConditionalOnBlockingDiscoveryEnabled;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiaojing
 * @author echooymxq
 * @author ruansheng
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnDiscoveryEnabled
@ConditionalOnBlockingDiscoveryEnabled
@ConditionalOnNacosDiscoveryEnabled
@AutoConfigureBefore({ SimpleDiscoveryClientAutoConfiguration.class,
		CommonsClientAutoConfiguration.class })
@AutoConfigureAfter(NacosDiscoveryAutoConfiguration.class)
public class NacosDiscoveryClientConfiguration {

	@Bean
	public DiscoveryClient nacosDiscoveryClient(
			NacosServiceDiscovery nacosServiceDiscovery) {
		return new NacosDiscoveryClient(nacosServiceDiscovery);
	}

	/**
	 * NacosWatch is no longer enabled by default.
	 * https://github.com/alibaba/spring-cloud-alibaba/issues/2868
	 * @param nacosServiceManager nacosServiceManager
	 * @param nacosDiscoveryProperties nacosDiscoveryProperties
	 * @return nacosWatch.
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty("spring.cloud.nacos.discovery.watch.enabled")
	public NacosWatch nacosWatch(NacosServiceManager nacosServiceManager,
			NacosDiscoveryProperties nacosDiscoveryProperties) {
		return new NacosWatch(nacosServiceManager, nacosDiscoveryProperties);
	}

	/**
	 * Spring Cloud Gateway HeartBeat . publish an event every 30 seconds.
	 * https://github.com/alibaba/spring-cloud-alibaba/issues/2868
	 * @param nacosDiscoveryProperties nacosDiscoveryProperties
	 * @return nacosWatch.
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty("spring.cloud.gateway.discovery.locator.enabled")
	public GatewayLocatorHeartBeatPublisher gatewayLocatorHeartBeatPublisher(
			NacosDiscoveryProperties nacosDiscoveryProperties) {
		return new GatewayLocatorHeartBeatPublisher(nacosDiscoveryProperties);
	}

}
