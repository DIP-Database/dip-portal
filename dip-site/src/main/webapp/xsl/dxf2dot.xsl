<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
     xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
   <!-- Take control of the whitespace. -->

   <xsl:output method="text" indent="no" encoding="UTF-8"/>
   <xsl:strip-space elements="*"/>
   <xsl:preserve-space elements="xsl:text"/>

   <!-- Copy comments, and elements recursively. -->

   <xsl:template match="dataset">
    <xsl:text>digraph dip {&#xa;</xsl:text>
    <xsl:text> size="5!,5!";&#xa;</xsl:text>
    <xsl:text> ratio = "expand";&#xa;</xsl:text>
    <xsl:text> bgcolor="#eeeeee";&#xa;</xsl:text>
    <xsl:text> node [shape = circle, width=0.2, height=0.2,style=filled, fillcolor=orange];&#xa;</xsl:text>
       <xsl:apply-templates select="node/partList/part[type/@ac = 'dxf:0010']" mode="node"/> 
       <xsl:apply-templates select="node[./type/@ac = 'dxf:0004']" mode="edge" />
    <xsl:text>}&#xa;</xsl:text>
   </xsl:template>

  <xsl:template mode="node"
       match="node"> 
        <xsl:text>"</xsl:text>
        <xsl:value-of select="@ac"/>
        <xsl:text>" [label="", fillcolor="#FFFF88",</xsl:text>
        <xsl:text> tooltip="</xsl:text>
        <xsl:value-of select="@ac"/>
        <xsl:text>", URL="rex?ns=dip&amp;ac=</xsl:text>
        <xsl:value-of select="@ac"/>
        <xsl:text>&amp;md=N&amp;nb=EX1%2B&amp;sl=2&amp;xf.grv=0" ];&#xa;</xsl:text>
  </xsl:template>

  <xsl:template mode="edge" 
        match="node[./type/@ac = 'dxf:0004']">  
     <xsl:if test="count(./partList/part) &lt; 3">
        <xsl:text>"</xsl:text>
        <xsl:value-of select="./partList/part[1]/node/@ac"/>
        <xsl:text>"->"</xsl:text>
        <xsl:choose>
          <xsl:when test="count(./partList/part) = 1">
            <xsl:value-of select="./partList/part[1]/node/@ac"/>
          </xsl:when>
          <xsl:otherwise>
             <xsl:value-of select="./partList/part[2]/node/@ac"/>
          </xsl:otherwise>
        </xsl:choose>
         <xsl:text>" [dir=none,weight=1,URL="rex?ns=dip&amp;ac=</xsl:text>
         <xsl:value-of select="@ac"/>
         <xsl:text>&amp;md=N&amp;nb=EX1%2B&amp;sl=2&amp;xf.grv=0"];&#xa;</xsl:text>
     </xsl:if>
   </xsl:template>
</xsl:stylesheet>
