<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="xml" indent="no"/>
	
	<xsl:template match="/">
		<result>
			<xsl:apply-templates/>
		</result>
	</xsl:template>
	
	<xsl:template match="@*|*|text()">
		<xsl:copy>
			<xsl:apply-templates select="@*"/>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>
	
	
</xsl:stylesheet>