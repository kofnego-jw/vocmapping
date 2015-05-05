/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.ac.uibk.igwee.metadata.vocabulary;

import java.net.URI;
import java.util.Set;

/**
 * Base interface that must be implemented to create a vocabulary.
 * @author joseph
 *
 */
public interface Vocabulary {
	
	/**
	 * Internal ID, should never be null and should -- if possible -- be unique.
	 * e.g. "01234567"
	 * @return
	 */
	public String getInternalID();
	
	/**
	 * The URI to the vocabulary. Should be brows-able.
	 * e.g. http://d-nb.info/gnd/118529579 for Albert Einstein
	 * @return
	 */
	public URI getURI();
	
	/**
	 * The name of the vocabulary, e.g. "Innsbruck", "Einstein, Albert", "Mathematics/Analysis"
	 * @return
	 */
	public String getName();
	
	/**
	 * The authority which maintains the vocabulary (e.g. GND)
	 * @return
	 */
	public Authority getAuthority();
	
	/**
	 * The type of the vocabulary
	 * @return
	 */
	public VocabularyType getVocabularyType();
	
	/**
	 * 
	 * @return a set of URIs that points to the same vocabulary
	 */
	public Set<Vocabulary> getKnownSameAs();
	
	/**
	 * Adds a same-as voc.
	 * @param uri
	 */
	public void addSameAs(Vocabulary uri);

	/**
	 * removes a same-as voc.
	 * @param uri
	 */
	public void removeSameAs(Vocabulary uri);
	
	

}