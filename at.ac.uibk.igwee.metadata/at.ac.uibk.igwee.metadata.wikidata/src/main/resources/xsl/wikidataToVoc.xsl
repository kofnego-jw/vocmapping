<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="xs"
    version="2.0">
    
    <xsl:param name="totalhits">-1</xsl:param>
    <xsl:param name="startrow">0</xsl:param>
    <xsl:param name="maxrows" select="count(//entity)"/>
    <xsl:param name="nextrow" select="$startrow + $maxrows + 1"></xsl:param>
    
    <xsl:output method="xml" indent="yes"/>
    
    
    
    <xsl:template match="/">
        <wikidataQueryResult>
            <totalhits><xsl:value-of select="$totalhits"/></totalhits>
            <startRow><xsl:value-of select="$startrow"/></startRow>
            <maxRows><xsl:value-of select="$maxrows"/></maxRows>
            <nextRow><xsl:value-of select="$nextrow"/></nextRow>
            <xsl:for-each select="//entity">
                <xsl:apply-templates select="."/>
            </xsl:for-each>
        </wikidataQueryResult>
    </xsl:template>
    
    <xsl:template match="entity">
        <xsl:variable name="wikidataId" select="@title"/>
        <xsl:variable name="vocType">
            <xsl:choose>
                <xsl:when test="./claims/property[@id='P31']/claim/mainsnak/datavalue/value[@numeric-id='5']">
                    <xsl:text>PERSONAL_NAME</xsl:text>
                </xsl:when>
                <xsl:when test="./claims/property[@id='P30'] or ./claims/property[@id='P17']">
                    <xsl:text>PLACE_NAME</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text>UNKNOWN</xsl:text>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <wikidataVocabulary>
            <id><xsl:text>wikidata:</xsl:text><xsl:value-of select="$wikidataId"/></id>
            <uri><xsl:text>http://www.wikidata.org/wiki/</xsl:text><xsl:value-of select="$wikidataId"/></uri>
            <vocabularyType><xsl:value-of select="$vocType"/></vocabularyType>
            <sameAsSet/>
            <wikidataId><xsl:value-of select="$wikidataId"/></wikidataId>
            <labels>
                <xsl:for-each select="labels/label">
                    <entry>
                        <string><xsl:value-of select="@language"/></string>
                        <string><xsl:value-of select="@value"/></string>
                    </entry>
                </xsl:for-each>
            </labels>
        </wikidataVocabulary>
    </xsl:template>
    
</xsl:stylesheet>