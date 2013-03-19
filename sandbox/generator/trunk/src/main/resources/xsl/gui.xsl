<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:jutils="http://code.google.com/p/j2ee-utils/xslt" xmlns:p="http://code.google.com/p/j2ee-utils/schema/project"
	xmlns:b="http://code.google.com/p/j2ee-utils/schema/java-beans"
	xmlns:h="http://code.google.com/p/j2ee-utils/schema/hibernate" xmlns:g="http://code.google.com/p/j2ee-utils/schema/gui">
	<xsl:output method="xml" indent="yes" />

	<xsl:template match="/">
		<p:project xmlns:p="http://code.google.com/p/j2ee-utils/schema/project"
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:b="http://code.google.com/p/j2ee-utils/schema/java-beans"
			xmlns:h="http://code.google.com/p/j2ee-utils/schema/hibernate"
			xmlns:g="http://code.google.com/p/j2ee-utils/schema/gui"
			xsi:schemaLocation="http://code.google.com/p/j2ee-utils/schema/project http://code.google.com/p/j2ee-utils/schema/project.xsd http://code.google.com/p/j2ee-utils/schema/java-beans http://code.google.com/p/j2ee-utils/schema/java-beans.xsd http://code.google.com/p/j2ee-utils/schema/hibernate http://code.google.com/p/j2ee-utils/schema/hibernate.xsd http://code.google.com/p/j2ee-utils/schema/gui http://code.google.com/p/j2ee-utils/schema/gui.xsd ">
			<p:configuration>
				<p:projectName>
					<xsl:value-of select="//p:projectName" />
				</p:projectName>
			</p:configuration>

			<xsl:if test="count(//h:entity) gt 0">
				<p:views>
					<xsl:apply-templates select="//h:entity" mode="form" />

					<xsl:apply-templates select="//h:entity" mode="datatable" />
				</p:views>
			</xsl:if>
		</p:project>
	</xsl:template>

	<xsl:template match="h:entity" mode="form">
		<xsl:variable name="package" select="./ancestor::p:package[1]/@name" />
		<xsl:variable name="entityName" select="./@name" />

		<g:form entity="{$package}.{$entityName}" name="{$entityName}">
			<g:fields>
				<xsl:apply-templates select="./h:id" mode="form" />
				<xsl:apply-templates select="./h:embedded-id"
					mode="form" />
				<xsl:apply-templates select="./h:properties/h:column"
					mode="form" />
				<xsl:apply-templates select="./h:properties/h:many-to-one"
					mode="form" />
			</g:fields>
		</g:form>
	</xsl:template>

	<xsl:template match="h:entity" mode="datatable">
		<xsl:variable name="package" select="./ancestor::p:package[1]/@name" />
		<xsl:variable name="entityName" select="./@name" />

		<g:datatable entity="{$package}.{$entityName}" name="{$entityName}">
			<g:columns>
				<xsl:apply-templates select="./h:id" mode="datatable" />
				<xsl:apply-templates select="./h:embedded-id"
					mode="datatable" />
				<xsl:apply-templates select="./h:properties/h:column"
					mode="datatable" />
				<xsl:apply-templates select="./h:properties/h:many-to-one"
					mode="datatable" />
			</g:columns>
			<g:filters>
				<xsl:apply-templates select="./h:embedded-id"
					mode="filter" />
				<xsl:apply-templates select="./h:properties/h:column"
					mode="filter" />
				<xsl:apply-templates select="./h:properties/h:many-to-one"
					mode="filter" />
			</g:filters>
		</g:datatable>
	</xsl:template>

	<xsl:template match="h:id" mode="form">
		<xsl:variable name="name" select="@name" />
		<xsl:variable name="label"
			select="jutils:uppercaseFirstCharacter(@name)" />
		<xsl:variable name="dataType" select="@type" />
		<xsl:variable name="value" select="@name" />

		<g:input-text create="false" readonlyOnUpdate="true"
			required="true">
			<xsl:attribute name="name" select="$name" />
			<xsl:attribute name="label" select="$label" />
			<xsl:attribute name="dataType" select="$dataType" />
			<xsl:attribute name="value" select="$value" />
			<xsl:if test="@length">
				<xsl:attribute name="maxlength" select="@length" />
			</xsl:if>
		</g:input-text>
	</xsl:template>


	<xsl:template match="h:id" mode="datatable">
		<xsl:variable name="name" select="@name" />
		<xsl:variable name="headerText"
			select="jutils:uppercaseFirstCharacter(@name)" />
		<xsl:variable name="dataType" select="@type" />
		<xsl:variable name="value" select="@name" />

		<g:column>
			<xsl:attribute name="name" select="$name" />
			<xsl:attribute name="headerText" select="$headerText" />
			<xsl:attribute name="dataType" select="$dataType" />
			<xsl:attribute name="value" select="$value" />
		</g:column>
	</xsl:template>

	<xsl:template match="h:embedded-id" mode="form">
		<xsl:variable name="propName" select="@name" />
		<xsl:for-each select="./h:properties/h:column">
			<xsl:variable name="name" select="@name" />
			<xsl:variable name="label"
				select="jutils:uppercaseFirstCharacter(@name)" />
			<xsl:variable name="dataType" select="@type" />
			<xsl:variable name="value" select="concat($propName, '.', @name)" />

			<g:input-text readonlyOnUpdate="true" required="true">
				<xsl:attribute name="name" select="$name" />
				<xsl:attribute name="label" select="$label" />
				<xsl:attribute name="dataType" select="$dataType" />
				<xsl:attribute name="value" select="$value" />
				<xsl:if test="@length">
					<xsl:attribute name="maxlength" select="@length" />
				</xsl:if>
			</g:input-text>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="h:embedded-id" mode="datatable">
		<xsl:variable name="propName" select="@name" />
		<xsl:for-each select="./h:properties/h:column">
			<xsl:variable name="name" select="@name" />
			<xsl:variable name="headerText"
				select="jutils:uppercaseFirstCharacter(@name)" />
			<xsl:variable name="dataType" select="@type" />
			<xsl:variable name="value" select="concat($propName, '.', @name)" />

			<g:column>
				<xsl:attribute name="name" select="$name" />
				<xsl:attribute name="headerText" select="$headerText" />
				<xsl:attribute name="dataType" select="$dataType" />
				<xsl:attribute name="value" select="$value" />
			</g:column>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="h:embedded-id" mode="filter">
		<xsl:variable name="propName" select="@name" />
		<xsl:for-each select="./h:properties/h:column">
			<xsl:variable name="name" select="@name" />
			<xsl:variable name="label"
				select="jutils:uppercaseFirstCharacter(@name)" />
			<xsl:variable name="dataType" select="@type" />
			<xsl:variable name="value" select="concat($propName, '.', @name)" />

			<g:filter-text>
				<xsl:attribute name="name" select="$name" />
				<xsl:attribute name="label" select="$label" />
				<xsl:attribute name="dataType" select="$dataType" />
				<xsl:attribute name="value" select="$value" />
			</g:filter-text>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="h:column" mode="form">
		<xsl:variable name="name" select="@name" />
		<xsl:variable name="label"
			select="jutils:uppercaseFirstCharacter(@name)" />
		<xsl:variable name="dataType" select="@type" />
		<xsl:variable name="value" select="@name" />

		<xsl:choose>
			<xsl:when test="@type = 'Boolean' or @type = 'boolean'">
				<g:select-one type="checkbox" dataType="Boolean"
					defaultLabel="false">
					<xsl:attribute name="name" select="$name" />
					<xsl:attribute name="label" select="$label" />
					<xsl:attribute name="value" select="$value" />
					<xsl:if test="@nullable = 'false'">
						<xsl:attribute name="required" select="'true'" />
					</xsl:if>
				</g:select-one>
			</xsl:when>
			<xsl:when test="@type = 'String' and @length and number(@length) gt 255">
				<g:input-textarea>
					<xsl:attribute name="name" select="@name" />
					<xsl:attribute name="label" select="$label" />
					<xsl:attribute name="value" select="@name" />
					<xsl:if test="@nullable = 'false'">
						<xsl:attribute name="required" select="'true'" />
					</xsl:if>
				</g:input-textarea>
			</xsl:when>
			<xsl:otherwise>
				<g:input-text>
					<xsl:attribute name="name" select="$name" />
					<xsl:attribute name="label" select="$label" />
					<xsl:attribute name="dataType" select="$dataType" />
					<xsl:attribute name="value" select="$value" />
					<xsl:if test="@nullable = 'false'">
						<xsl:attribute name="required" select="'true'" />
					</xsl:if>
					<xsl:if test="@length">
						<xsl:attribute name="maxlength" select="@length" />
					</xsl:if>
				</g:input-text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="h:column" mode="datatable">
		<xsl:variable name="name" select="@name" />
		<xsl:variable name="headerText"
			select="jutils:uppercaseFirstCharacter(@name)" />
		<xsl:variable name="dataType" select="@type" />
		<xsl:variable name="value" select="@name" />

		<xsl:choose>
			<xsl:when test="@type = 'Boolean' or @type = 'boolean'">
				<g:column>
					<xsl:attribute name="name" select="$name" />
					<xsl:attribute name="headerText" select="$headerText" />
					<xsl:attribute name="dataType" select="$dataType" />
					<xsl:attribute name="value" select="$value" />
				</g:column>
			</xsl:when>
			<xsl:when test="@type = 'String' and @length and number(@length) gt 255">
				<g:column>
					<xsl:attribute name="name" select="$name" />
					<xsl:attribute name="headerText" select="$headerText" />
					<xsl:attribute name="dataType" select="$dataType" />
					<xsl:attribute name="value" select="$value" />
				</g:column>
			</xsl:when>
			<xsl:otherwise>
				<g:column>
					<xsl:attribute name="name" select="$name" />
					<xsl:attribute name="headerText" select="$headerText" />
					<xsl:attribute name="dataType" select="$dataType" />
					<xsl:attribute name="value" select="$value" />
				</g:column>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="h:column" mode="filter">
		<xsl:variable name="name" select="@name" />
		<xsl:variable name="label"
			select="jutils:uppercaseFirstCharacter(@name)" />
		<xsl:variable name="dataType" select="@type" />
		<xsl:variable name="value" select="@name" />

		<xsl:choose>
			<xsl:when test="@type = 'Boolean' or @type = 'boolean'">
				<g:filter-one type="checkbox" dataType="Boolean"
					defaultLabel="false">
					<xsl:attribute name="name" select="$name" />
					<xsl:attribute name="label" select="$label" />
					<xsl:attribute name="value" select="$value" />
				</g:filter-one>
			</xsl:when>
			<xsl:otherwise>
				<g:filter-text>
					<xsl:attribute name="name" select="$name" />
					<xsl:attribute name="label" select="$label" />
					<xsl:attribute name="dataType" select="$dataType" />
					<xsl:attribute name="value" select="$value" />
				</g:filter-text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="h:many-to-one" mode="form">
		<xsl:variable name="name" select="@name" />
		<xsl:variable name="label"
			select="jutils:uppercaseFirstCharacter(@name)" />
		<xsl:variable name="dataType" select="@targetEntity" />
		<xsl:variable name="value" select="@name" />

		<g:select-one type="list">
			<xsl:attribute name="name" select="$name" />
			<xsl:attribute name="label" select="$label" />
			<xsl:attribute name="dataType" select="$dataType" />
			<xsl:attribute name="value" select="$value" />
			<xsl:if test="@nullable = 'false'">
				<xsl:attribute name="required" select="'true'" />
			</xsl:if>
		</g:select-one>
	</xsl:template>

	<xsl:template match="h:many-to-one" mode="datatable">
		<xsl:variable name="name" select="@name" />
		<xsl:variable name="headerText"
			select="jutils:uppercaseFirstCharacter(@name)" />
		<xsl:variable name="dataType" select="@targetEntity" />
		<xsl:variable name="value" select="@name" />

		<g:column>
			<xsl:attribute name="name" select="$name" />
			<xsl:attribute name="headerText" select="$headerText" />
			<xsl:attribute name="dataType" select="$dataType" />
			<xsl:attribute name="value" select="$value" />
		</g:column>
	</xsl:template>

	<xsl:template match="h:many-to-one" mode="filter">
		<xsl:variable name="name" select="@name" />
		<xsl:variable name="label"
			select="jutils:uppercaseFirstCharacter(@name)" />
		<xsl:variable name="dataType" select="@targetEntity" />
		<xsl:variable name="value" select="@name" />

		<g:filter-one type="list">
			<xsl:attribute name="name" select="$name" />
			<xsl:attribute name="label" select="$label" />
			<xsl:attribute name="dataType" select="$dataType" />
			<xsl:attribute name="value" select="$value" />
		</g:filter-one>
	</xsl:template>

	<xsl:function name="jutils:uppercaseFirstCharacter" as="xs:string">
		<xsl:param name="value" as="xs:string" />

		<xsl:value-of
			select="concat(upper-case(substring($value,1,1)),substring($value, 2))" />
	</xsl:function>

</xsl:stylesheet>