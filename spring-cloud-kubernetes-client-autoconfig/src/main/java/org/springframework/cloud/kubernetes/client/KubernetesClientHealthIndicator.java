/*
 * Copyright 2013-2020 the original author or authors.
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

package org.springframework.cloud.kubernetes.client;

import java.util.Collections;
import java.util.Map;

import io.kubernetes.client.openapi.models.V1Pod;

import org.springframework.cloud.kubernetes.commons.AbstractKubernetesHealthIndicator;
import org.springframework.cloud.kubernetes.commons.PodUtils;
import org.springframework.util.CollectionUtils;

/**
 * @author Ryan Baxter
 */
public class KubernetesClientHealthIndicator extends AbstractKubernetesHealthIndicator {

	private final PodUtils<V1Pod> utils;

	public KubernetesClientHealthIndicator(PodUtils<V1Pod> utils) {
		this.utils = utils;
	}

	@Override
	protected Map<String, Object> getDetails() {
		V1Pod current = this.utils.currentPod().get();
		if (current != null) {
			Map<String, Object> details = CollectionUtils.newHashMap(8);
			details.put(INSIDE, true);
			details.put(NAMESPACE, current.getMetadata().getNamespace());
			details.put(POD_NAME, current.getMetadata().getName());
			details.put(LABELS, current.getMetadata().getLabels());
			details.put(POD_IP, current.getStatus().getPodIP());
			details.put(HOST_IP, current.getStatus().getHostIP());
			details.put(SERVICE_ACCOUNT, current.getSpec().getServiceAccountName());
			details.put(NODE_NAME, current.getSpec().getNodeName());
			return details;
		}
		else {
			return Collections.singletonMap(INSIDE, false);
		}
	}

}
