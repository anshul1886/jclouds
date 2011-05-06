/**
 *
 * Copyright (C) 2011 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */
package org.jclouds.aws.s3.binders;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.net.URI;

import javax.ws.rs.HttpMethod;

import org.jclouds.blobstore.binders.BindMapToHeadersWithPrefix;
import org.jclouds.http.HttpRequest;
import org.jclouds.rest.internal.RestAnnotationProcessor;
import org.jclouds.s3.BaseS3AsyncClientTest;
import org.jclouds.s3.S3AsyncClient;
import org.jclouds.s3.domain.ObjectMetadata;
import org.jclouds.s3.domain.ObjectMetadataBuilder;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.inject.TypeLiteral;

/**
 * Tests behavior of {@code BindObjectMetadataToRequest}
 * 
 * @author Adrian Cole
 */
// NOTE:without testName, this will not call @Before* and fail w/NPE during surefire
@Test(groups = "unit", testName = "BindObjectMetadataToRequestTest")
public class BindObjectMetadataToRequestTest extends BaseS3AsyncClientTest<S3AsyncClient> {

   @Override
   protected TypeLiteral<RestAnnotationProcessor<S3AsyncClient>> createTypeLiteral() {
      return new TypeLiteral<RestAnnotationProcessor<S3AsyncClient>>() {
      };
   }

   @Test
   public void testPassWithMinimumDetailsAndPayload5GB() {
      ObjectMetadata md = ObjectMetadataBuilder.create().key("foo").build();

      HttpRequest request = HttpRequest.builder().method("POST").endpoint(URI.create("http://localhost")).build();
      BindObjectMetadataToRequest binder = injector.getInstance(BindObjectMetadataToRequest.class);

      assertEquals(binder.bindToRequest(request, md), HttpRequest.builder().method("POST").endpoint(
               URI.create("http://localhost")).headers(ImmutableMultimap.of("Content-Type", "binary/octet-stream"))
               .build());
   }

   @Test
   public void testExtendedPropertiesBind() {
      ObjectMetadata md = ObjectMetadataBuilder.create().key("foo").cacheControl("no-cache").userMetadata(
               ImmutableMap.of("foo", "bar")).build();

      HttpRequest request = HttpRequest.builder().method("POST").endpoint(URI.create("http://localhost")).build();
      BindObjectMetadataToRequest binder = injector.getInstance(BindObjectMetadataToRequest.class);

      assertEquals(binder.bindToRequest(request, md), HttpRequest.builder().method("POST").endpoint(
               URI.create("http://localhost")).headers(
               ImmutableMultimap.of("Cache-Control", "no-cache", "x-amz-meta-foo", "bar", "Content-Type",
                        "binary/octet-stream")).build());
   }

   @Test(expectedExceptions = IllegalArgumentException.class)
   public void testNoKeyIsBad() {
      ObjectMetadata md = ObjectMetadataBuilder.create().build();

      HttpRequest request = HttpRequest.builder().method("POST").endpoint(URI.create("http://localhost")).build();
      BindObjectMetadataToRequest binder = injector.getInstance(BindObjectMetadataToRequest.class);
      binder.bindToRequest(request, md);
   }

   @Test(expectedExceptions = IllegalArgumentException.class)
   public void testMustBeObjectMetadata() {
      HttpRequest request = new HttpRequest(HttpMethod.POST, URI.create("http://localhost"));
      injector.getInstance(BindObjectMetadataToRequest.class).bindToRequest(request, new File("foo"));
   }

   @Test(expectedExceptions = { NullPointerException.class, IllegalStateException.class })
   public void testNullIsBad() {
      BindMapToHeadersWithPrefix binder = new BindMapToHeadersWithPrefix("prefix:");
      HttpRequest request = HttpRequest.builder().method("GET").endpoint(URI.create("http://momma")).build();
      binder.bindToRequest(request, null);
   }
}