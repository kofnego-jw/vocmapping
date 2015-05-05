<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="xs"
    version="2.0">
    
    <xsl:param name="startRow">1</xsl:param>
    <xsl:param name="maxRows">-1</xsl:param>
    
    <xsl:output method="xml" indent="yes"/>
    
    <xsl:template match="/">
        <xsl:apply-templates select="//geoname"/>
    </xsl:template>
    
    <xsl:template match="geoname">
        <xsl:variable name="geonameId" select="geonameId"/>
        <geonameData>
            <id><xsl:text>geonames:</xsl:text><xsl:value-of select="$geonameId"/></id>
            <uri><xsl:text>http://geonames.org/</xsl:text><xsl:value-of select="$geonameId"/></uri>
            <vocabularyType>PLACE_NAME</vocabularyType>
            <sameAsSet/>
            <name><xsl:value-of select="toponymName"/></name>
            <country><xsl:value-of select="countryName"/></country>
            <countryCode><xsl:value-of select="countryCode"/></countryCode>
            <continentCode><xsl:value-of select="continentCode"/></continentCode>
            <longitude><xsl:value-of select="lng"/></longitude>
            <latitude><xsl:value-of select="lat"/></latitude>
            <geonameId><xsl:value-of select="$geonameId"/></geonameId>
        </geonameData>
    </xsl:template>
    
</xsl:stylesheet>