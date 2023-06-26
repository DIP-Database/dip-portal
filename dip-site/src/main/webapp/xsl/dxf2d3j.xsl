<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
     xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
   <!-- Take control of the whitespace. -->

   <xsl:output method="text" indent="no" encoding="UTF-8"/>
   <xsl:strip-space elements="*"/>
   <xsl:preserve-space elements="xsl:text"/>

   <!-- Copy comments, and elements recursively. -->

   <xsl:template match="dataset">
    <xsl:text>{&#xa;</xsl:text>
    <xsl:text> "node":[&#xa;</xsl:text>
       <xsl:apply-templates  mode="node"
         select="(node/partList[count(./part) &lt; 3]/part[type/@ac = 'dxf:0010']/node[not(@ac = preceding::node/partList[count(./part) &lt; 3]/part[type/@ac = 'dxf:0010']/node/@ac)      ])[position() &lt; last()]">
<!--         <xsl:sort select="@ac"/> -->
       </xsl:apply-templates>
       <xsl:apply-templates  mode="node"
         select="(node/partList[count(./part) &lt; 3]/part[type/@ac = 'dxf:0010']/node[not(@ac = preceding::node/partList[count(./part) &lt; 3]/part[type/@ac = 'dxf:0010']/node/@ac)      ])[position() = last()]">
<!--         <xsl:sort select="@ac"/> -->
         <xsl:with-param name="separator"></xsl:with-param>
       </xsl:apply-templates>
    <xsl:text> ],&#xa;</xsl:text>  
    <xsl:text> "edge":[&#xa;</xsl:text>
     <xsl:apply-templates  mode="edge" 
        select="(node[./type/@ac = 'dxf:0004'])[position() &lt; last()]">
        <xsl:sort select="@ac"/>
     </xsl:apply-templates>
     <xsl:apply-templates mode="edge" 
        select="(node[./type/@ac = 'dxf:0004'])[position() = last()]">
        <xsl:sort select="@ac"/>
        <xsl:with-param name="separator"></xsl:with-param> 
     </xsl:apply-templates>

    <xsl:text> ]&#xa;</xsl:text>
    <xsl:text>}&#xa;</xsl:text>
   </xsl:template>

  <xsl:template mode="node" match="node"> 
     <xsl:param name="separator">,&#xa;</xsl:param>
        <xsl:text>   {"key":"</xsl:text>
         <xsl:value-of select="@ac"/>
        <xsl:text>", "ac":"</xsl:text>
         <xsl:value-of select="@ac"/>
        <xsl:text>", "tt":"</xsl:text>
         <xsl:value-of select="@ac"/>
        <xsl:text>", "url":"rex?ns=dip&amp;ac=</xsl:text>
        <xsl:value-of select="@ac"/>
        <xsl:text>&amp;md=N&amp;nb=EX1%2B&amp;sl=2&amp;xf.d3j=0" }</xsl:text>
        <xsl:value-of select="$separator"/>
  </xsl:template>

  <xsl:template mode="edge" 
        match="node[./type/@ac = 'dxf:0004']">
     <xsl:param name="separator">,&#xa;</xsl:param>
     <xsl:if test="count(./partList/part) &lt; 3">
        <xsl:text>   {"key":"</xsl:text>
          <xsl:value-of select="@ac"/>
        <xsl:text>", "source":"</xsl:text>
         <xsl:value-of select="./partList/part[1]/node/@ac"/>
        <xsl:text>", "target":"</xsl:text>
        <xsl:choose>
          <xsl:when test="count(./partList/part) = 1">
            <xsl:value-of select="./partList/part[1]/node/@ac"/>
          </xsl:when>
          <xsl:otherwise>
             <xsl:value-of select="./partList/part[2]/node/@ac"/>
          </xsl:otherwise>
        </xsl:choose>
         <xsl:text>", "url":"rex?ns=dip&amp;ac=</xsl:text>
         <xsl:value-of select="@ac"/>
         <xsl:text>&amp;md=N&amp;nb=EX1%2B&amp;sl=2&amp;xf.grv=0"}</xsl:text>
         <xsl:value-of select="$separator"/>
     </xsl:if>
   </xsl:template>
</xsl:stylesheet>
