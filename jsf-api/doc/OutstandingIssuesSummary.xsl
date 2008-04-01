<?xml version="1.0" encoding="utf-8"?>
<!-- Content Stylesheet for Outstanding Issues Summary List -->
<!-- $Id: OutstandingIssuesSummary.xsl,v 1.1 2002/09/05 16:53:52 craigmcc Exp $ -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">

  <xsl:output method="html"/>

  <xsl:template match="issues">
    <html>
      <head>
        <title>JavaServer Faces 1.0 - Outstanding Issues Summary</title>
      </head>
      <body bgcolor="#FFFFFF">
        <table border="1" width="100%" cellpadding="5">
          <tr>
            <th width="10%">Issue</th>
            <th width="55%">Summary</th>
            <th width="10%">Source</th>
            <th width="10%">Status</th>
            <th width="15%">Date</th>
          </tr>
          <xsl:apply-templates select="issue"/>
        </table>
      </body>
    </html>
  </xsl:template>

  <xsl:template match="issue">
    <tr>
      <td><xsl:value-of select="id"/></td>
      <td><xsl:value-of select="summary"/></td>
      <td><xsl:value-of select="originator"/></td>
      <td align="center"><xsl:value-of select="status"/></td>
      <td align="center"><xsl:value-of select="status-date"/></td>
    </tr>
  </xsl:template>

</xsl:stylesheet>
