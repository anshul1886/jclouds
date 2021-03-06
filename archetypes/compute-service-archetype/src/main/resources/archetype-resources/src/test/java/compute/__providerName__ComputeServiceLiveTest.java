#set( $lcaseProviderName = ${providerName.toLowerCase()} )
#set( $camelCaseProviderName = "${providerName.substring(0, 1).toLowerCase()}${providerName.substring(1)}" )
/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package ${package}.compute;

import static org.testng.Assert.assertEquals;

import ${package}.${providerName}AsyncClient;
import ${package}.${providerName}Client;
import org.jclouds.compute.BaseComputeServiceLiveTest;
import org.jclouds.compute.ComputeServiceContextFactory;
import org.jclouds.compute.domain.Architecture;
import org.jclouds.compute.domain.OsFamily;
import org.jclouds.compute.domain.Template;
import org.jclouds.rest.RestContext;
import org.jclouds.ssh.jsch.config.JschSshClientModule;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 * @author ${author}
 */
@Test(groups = "live", enabled = true, sequential = true, testName = "${lcaseProviderName}.${providerName}ComputeServiceLiveTest")
public class ${providerName}ComputeServiceLiveTest extends BaseComputeServiceLiveTest {

       @BeforeClass
       @Override
       public void setServiceDefaults() {
          provider = "${lcaseProviderName}";
       }

       @Test
       public void testTemplateBuilder() {
          Template defaultTemplate = client.templateBuilder().build();
          assertEquals(defaultTemplate.getImage().getArchitecture(), Architecture.X86_64);
          assertEquals(defaultTemplate.getImage().getOperatingSystem().getFamily(), OsFamily.UBUNTU);
          assertEquals(defaultTemplate.getLocation().getId(), "DFW1");
          assertEquals(defaultTemplate.getSize().getCores(), 1.0d);
       }

       @Override
       protected JschSshClientModule getSshModule() {
          return new JschSshClientModule();
       }

       public void testAssignability() throws Exception {
          @SuppressWarnings("unused")
          RestContext<${providerName}Client, ${providerName}AsyncClient> tmContext = new ComputeServiceContextFactory()
                   .createContext(provider, user, password).getProviderSpecificContext();
       }

    }
