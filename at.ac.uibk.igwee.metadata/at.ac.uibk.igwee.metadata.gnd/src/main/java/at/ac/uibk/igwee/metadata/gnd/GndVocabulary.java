package at.ac.uibk.igwee.metadata.gnd;

import at.ac.uibk.igwee.metadata.vocabulary.AbstractVocabulary;
import at.ac.uibk.igwee.metadata.vocabulary.VocabularyType;

public class GndVocabulary extends AbstractVocabulary {
	
	private String gndId;
	
	private String name;
	
	public GndVocabulary() {
		super();
	}
	
	public GndVocabulary(String gndId, String name, VocabularyType type) {
		super("gnd:" + gndId, DeDnb.getInstance().createURI(gndId), DeDnb.getInstance(), type, null);
		this.gndId = gndId;
		this.name = name;
	}
	
	/**
	 * @return the gndId
	 */
	public String getGndId() {
		return gndId;
	}

	/**
	 * @param gndId the gndId to set
	 */
	public void setGndId(String gndId) {
		this.gndId = gndId;
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	public void setName(String n) {
		this.name = n;
	}
	
	private Object readResolve() {
		if (this.getAuthority()==null) {
			this.setAuthority(DeDnb.getInstance());
		}
		return this;
	}

}
