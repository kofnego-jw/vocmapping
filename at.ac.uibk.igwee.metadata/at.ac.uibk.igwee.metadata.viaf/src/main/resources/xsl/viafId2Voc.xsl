<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:srw="http://www.loc.gov/zing/srw/"
    xmlns:ns2="http://viaf.org/viaf/terms#"
    
    
    exclude-result-prefixes="xs xsl xsd xsi srw"
    version="2.0">
    
    <xsl:param name="viafBase">http://www.viaf.org/viaf/</xsl:param>
    
    <xsl:output method="xml" indent="yes"/>
    
    
    
    
    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>
    
    <xsl:template match="ns2:VIAFCluster">
        
        <xsl:variable name="viafID" select="ns2:viafID"/>
        <xsl:variable name="vocType" select="ns2:nameType"/>
        <viafVocabulary>
            <id>
                <xsl:text>viaf:</xsl:text>
                <xsl:value-of select="$viafID"/>
            </id>
            <uri>
                <xsl:value-of select="$viafBase"/>
                <xsl:value-of select="$viafID"/>
            </uri>
            <vocabularyType>
                <xsl:choose>
                    <xsl:when test="$vocType='Personal'">
                        <xsl:text>PERSONAL_NAME</xsl:text>
                    </xsl:when>
                    <xsl:when test="$vocType='Corporate'">
                        <xsl:text>INSTITUTION_NAME</xsl:text>
                    </xsl:when>
                    <xsl:when test="$vocType='Geographic'">
                        <xsl:text>PLACE_NAME</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:text>UNKNOWN</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
            </vocabularyType>
            <sameAsSet/>
            <name>
                <xsl:value-of select="(.//ns2:mainHeadings/ns2:data/ns2:text)[1]"/>
            </name>
            <linkedSources>
                <xsl:for-each select="ns2:sources/ns2:source">
                    <xsl:variable name="wholeID" select="."/>
                    <xsl:variable name="sourceKey" select="substring-before($wholeID, '|')"/>
                    <xsl:variable name="sourceEntry" select="substring-after($wholeID, '|')"/>
                    <entry>
                        <string><xsl:value-of select="$sourceKey"/></string>
                        <string><xsl:value-of select="$sourceEntry"/></string>
                    </entry>
                </xsl:for-each>
            </linkedSources>
        </viafVocabulary>
        
    </xsl:template>
    
    
</xsl:stylesheet>