<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:excel="urn:schemas-microsoft-com:office:spreadsheet"
    xmlns:o="urn:schemas-microsoft-com:office:office"
    xmlns:x="urn:schemas-microsoft-com:office:excel"
    xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet"
    xmlns:html="http://www.w3.org/TR/REC-html40"
    exclude-result-prefixes="xs excel o x ss html"
    version="2.0">
    
    <xsl:output method="xml" indent="yes"/>
    
    <xsl:template match="/">
        <queryQueue>
            <name>queryqueue</name>
            <additionalInfo>queryqueue from excel xml</additionalInfo>
            <pendingQueries>
                <xsl:variable name="columnPositions">
                    <xsl:call-template name="columnPositions"/>
                </xsl:variable>
                <xsl:for-each select="//excel:Row[preceding-sibling::excel:Row]">
                    <xsl:call-template name="createVocQueue">
                        <xsl:with-param name="columnPosition" select="$columnPositions"/>
                        <xsl:with-param name="row" select="."/>
                    </xsl:call-template>
                </xsl:for-each>
            </pendingQueries>
            <results/>
            <lastEditingPosition>0</lastEditingPosition>
        </queryQueue>
    </xsl:template>
    
    <xsl:template name="createVocQueue">
        <xsl:param name="columnPosition"/>
        <xsl:param name="row"/>
        <xsl:variable name="type">
            <xsl:call-template name="findType">
                <xsl:with-param name="hint" select="$row/excel:Cell[position()=$columnPosition//Type]/excel:Data"></xsl:with-param>
            </xsl:call-template>
        </xsl:variable>
        <xsl:variable name="authorities">
            <xsl:call-template name="findAuthorities">
                <xsl:with-param name="hint" select="$row/excel:Cell[position()=$columnPosition//Authorities]/excel:Data"></xsl:with-param>
            </xsl:call-template>
        </xsl:variable>
        <vocabularyQuery>
            <type><xsl:value-of select="$type"/></type>
            <queryString><xsl:value-of select="$row/excel:Cell[position()=$columnPosition//Query]/excel:Data"/></queryString>
            <xsl:if test="count($authorities/authorities/child::*) &gt; 0">
                <xsl:for-each select="$authorities/authorities/child::*">
                    <xsl:copy/>
                </xsl:for-each>
            </xsl:if>
            <startRow>1</startRow>
            <maxRow>30</maxRow>
            <name><xsl:choose>
                <xsl:when test="$row//Key">
                    <xsl:value-of select="$row/excel:Cell[position()=$columnPosition//Key]/excel:Data"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$row/excel:Cell[position()=$columnPosition//Name]/excel:Data"/>
                </xsl:otherwise>
            </xsl:choose></name>
            <additionalInfo>
                <xsl:value-of select="$row/excel:Cell[position()=$columnPosition//Info]/excel:Data"/>
            </additionalInfo>
        </vocabularyQuery>
        
        
        
    </xsl:template>
    
    
    <xsl:template name="findType">
        <xsl:param name="hint"/>
        <xsl:variable name="h" select="lower-case($hint)"/>
        <xsl:choose>
            <xsl:when test="contains($h,'pers')">
                <xsl:text>PERSONAL_NAME</xsl:text>
            </xsl:when>
            <xsl:when test="contains($h,'plac')">
                <xsl:text>PLACE_NAME</xsl:text>
            </xsl:when>
            <xsl:when test="contains($h,'inst')">
                <xsl:text>INSTITUTION_NAME</xsl:text>
            </xsl:when>
            <xsl:when test="contains($h,'corp')">
                <xsl:text>INSTITUTION_NAME</xsl:text>
            </xsl:when>
            <xsl:otherwise>
                <xsl:text>UNKNOWN</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template name="findAuthorities">
        <xsl:param name="hint"/>
        <xsl:variable name="h" select="lower-case($hint)"/>
        <authorities>
            <xsl:if test="contains($h, 'geon')">
                <geonames/>
            </xsl:if>
            <xsl:if test="contains($h, 'viaf')">
                <viaf/>
            </xsl:if>
            <xsl:if test="contains($h, 'wikidata')">
                <wikidata/>
            </xsl:if>
        </authorities>
    </xsl:template>
    
    <xsl:template name="columnPositions">
        <xsl:variable name="firstRow" select="(//excel:Row)[1]"/>
        <nameRow>
            <xsl:if test="$firstRow/excel:Cell/excel:Data[contains(lower-case(.),'name')]">
                <Name>
                    <xsl:value-of select="count($firstRow/excel:Cell[excel:Data/contains(lower-case(.),'name')]/preceding-sibling::*) + 1"/>
                </Name>
            </xsl:if>
            <xsl:if test="$firstRow/excel:Cell/excel:Data[contains(lower-case(.),'key')]">
                <Key>
                    <xsl:value-of select="count($firstRow/excel:Cell[excel:Data/contains(lower-case(.),'key')]/preceding-sibling::*) + 1"/>
                </Key>
            </xsl:if>
            <xsl:if test="$firstRow/excel:Cell/excel:Data[contains(lower-case(.),'query')]">
                <Query>
                    <xsl:value-of select="count($firstRow/excel:Cell[excel:Data/contains(lower-case(.),'query')]/preceding-sibling::*) + 1"/>
                </Query>
            </xsl:if>
            <xsl:if test="$firstRow/excel:Cell/excel:Data[contains(lower-case(.),'info')]">
                <Info>
                    <xsl:value-of select="count($firstRow/excel:Cell[excel:Data/contains(lower-case(.),'info')]/preceding-sibling::*) + 1"/>
                </Info>
            </xsl:if>
            <xsl:if test="$firstRow/excel:Cell/excel:Data[contains(lower-case(.),'type')]">
                <Type>
                    <xsl:value-of select="count($firstRow/excel:Cell[excel:Data/contains(lower-case(.),'type')]/preceding-sibling::*) + 1"/>
                </Type>
            </xsl:if>
            <xsl:if test="$firstRow/excel:Cell/excel:Data[contains(lower-case(.),'auth')]">
                <Authorities>
                    <xsl:value-of select="count($firstRow/excel:Cell[excel:Data/contains(lower-case(.),'auth')]/preceding-sibling::*) + 1"/>
                </Authorities>
            </xsl:if>
        </nameRow>
    </xsl:template>
    
    
</xsl:stylesheet>