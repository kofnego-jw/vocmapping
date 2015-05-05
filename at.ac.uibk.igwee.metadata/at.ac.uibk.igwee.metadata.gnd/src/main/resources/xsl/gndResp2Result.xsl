<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:dc="http://purl.org/dc/elements/1.1/"
        xmlns:geo="http://www.opengis.net/ont/geosparql#"
        xmlns:foaf="http://xmlns.com/foaf/0.1/"
        xmlns:sf="http://www.opengis.net/ont/sf#"
        xmlns:isbd="http://iflastandards.info/ns/isbd/elements/"
        xmlns:gndo="http://d-nb.info/standards/elementset/gnd#"
        xmlns:dcterms="http://purl.org/dc/terms/"
        xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
        xmlns:marcRole="http://id.loc.gov/vocabulary/relators/"
        xmlns:lib="http://purl.org/library/"
        xmlns:umbel="http://umbel.org/umbel#"
        xmlns:rdau="http://rdaregistry.info/Elements/u/"
        xmlns:bibo="http://purl.org/ontology/bibo/"
        xmlns:owl="http://www.w3.org/2002/07/owl#"
        xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
        xmlns:skos="http://www.w3.org/2004/02/skos/core#"
        xmlns:srw="http://www.loc.gov/zing/srw/"
        exclude-result-prefixes="#all"
        version="2.0">

    <xsl:output method="xml" indent="yes"/>

    <xsl:template match="/">
        <gndQueryResult>
            <maxRows>
                <xsl:choose>
                    <xsl:when test="//srw:echoedSearchRetrieveRequest/srw:maximumRecords">
                        <xsl:value-of select="//srw:echoedSearchRetrieveRequest/srw:maximumRecords"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <!-- Default max rows: 30 -->
                        <xsl:text>30</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
            </maxRows>
            <startRow>
                <xsl:choose>
                    <xsl:when test="//srw:echoedSearchRetrieveRequest/srw:startRecord">
                        <xsl:value-of select="//srw:echoedSearchRetrieveRequest/srw:startRecord"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <!-- Default start row: 1 -->
                        <xsl:text>1</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
            </startRow>
            <nextRow>
                <xsl:value-of select="//srw:nextRecordPosition"/>
            </nextRow>
            <totalhits><xsl:value-of select="//srw:numberOfRecords"/></totalhits>
            <results>
                <xsl:apply-templates select="/srw:searchRetrieveResponse/srw:records"/>
            </results>
        </gndQueryResult>
    </xsl:template>

    <xsl:template match="srw:records">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="srw:record">
        <xsl:variable name="gndId" select="(srw:recordData//gndo:gndIdentifier)[1]"/>
        <gndVocabulary>
            <id><xsl:text>gnd:</xsl:text><xsl:value-of select="$gndId"/></id>
            <uri><xsl:text>http://d-nb.info/gnd/</xsl:text><xsl:value-of select="$gndId"/></uri>
            <xsl:choose>
                <xsl:when test="srw:recordData/rdf:RDF/rdf:Description/gndo:preferredNameForThePerson">
                    <vocabularyType>PERSONAL_NAME</vocabularyType>
                    <name><xsl:value-of select="srw:recordData/rdf:RDF/rdf:Description/gndo:preferredNameForThePerson"/></name>
                </xsl:when>
                <xsl:when test="srw:recordData/rdf:RDF/rdf:Description/gndo:preferredNameForTheCorporateBody">
                    <vocabularyType>INSTITUTION_NAME</vocabularyType>
                    <name><xsl:value-of select="srw:recordData/rdf:RDF/rdf:Description/gndo:preferredNameForTheCorporateBody"/></name>
                </xsl:when>
                <xsl:when test="srw:recordData/rdf:RDF/rdf:Description/gndo:preferredNameForThePlaceOrGeographicName">
                    <vocabularyType>PLACE_NAME</vocabularyType>
                    <name><xsl:value-of select="srw:recordData/rdf:RDF/rdf:Description/gndo:preferredNameForThePlaceOrGeographicName"/></name>
                </xsl:when>
                <xsl:otherwise>
                    <vocabularyType>UNKNOWN</vocabularyType>
                    <name>
                        <xsl:variable name="name">
                            <xsl:for-each select="srw:recordData//gndo:*">
                                <xsl:if test="starts-with(name(),'preferred')">
                                    <xsl:text>"</xsl:text>
                                    <xsl:value-of select="."/>
                                    <xsl:text>" </xsl:text>
                                </xsl:if>
                            </xsl:for-each>
                        </xsl:variable>
                        <xsl:value-of select="normalize-space($name)"/>
                    </name>
                </xsl:otherwise>
            </xsl:choose>
            <gndId><xsl:value-of select="$gndId"/></gndId>
            <sameAsSet />
        </gndVocabulary>
    </xsl:template>


</xsl:stylesheet>