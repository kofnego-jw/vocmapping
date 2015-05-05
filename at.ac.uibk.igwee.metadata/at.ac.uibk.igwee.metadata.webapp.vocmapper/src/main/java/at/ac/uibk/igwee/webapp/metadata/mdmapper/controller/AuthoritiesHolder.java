package at.ac.uibk.igwee.webapp.metadata.mdmapper.controller;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import at.ac.uibk.igwee.metadata.geonames.OrgGeonames;
import at.ac.uibk.igwee.metadata.gnd.DeDnb;
import at.ac.uibk.igwee.metadata.viaf.OrgViaf;
import at.ac.uibk.igwee.metadata.vocabulary.Authority;
import at.ac.uibk.igwee.metadata.wikidata.OrgWikidata;

@Component
@Scope("session")
public class AuthoritiesHolder {
	
	Set<Authority> usedAuthorities = new HashSet<>();
	
	public AuthoritiesHolder() {
		usedAuthorities.add(OrgGeonames.getInstance());
		usedAuthorities.add(DeDnb.getInstance());
		usedAuthorities.add(OrgViaf.getInstance());
		usedAuthorities.add(OrgWikidata.getInstance());
		
	}
	
	public void addAuthority(Authority n) {
		this.usedAuthorities.add(n);
	}
	
	public void removeAuthority(Authority n) {
		this.usedAuthorities.remove(n);
	}

	public void setAuthorities(Collection<Authority> auths) {
		this.usedAuthorities.clear();
		this.usedAuthorities.addAll(usedAuthorities);
	}

	public Set<Authority> getUsedAuthorities() {
		return this.usedAuthorities;
	}
}
