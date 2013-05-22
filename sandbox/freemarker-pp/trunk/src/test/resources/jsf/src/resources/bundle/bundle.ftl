<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "/common/common.inc" as util />
<@dropOutputFile />
<#assign entities = xml["//j:entity"]/>
<#assign projectName = xml["//p:configuration/p:projectName"]/>

<@resolveKey map=config key="resourceBundleFilePath" values=[projectName] assignTo="filePath"/>
<@resolveKey map=config key="resourceBundleFileName" values=[projectName] assignTo="fileName"/>
<@changeOutputFile name=filePath + "/"+ fileName />
# header
login_link=Se connecter
logout_link=Se déconnecter
global_head_title=${projectName?cap_first}
# header

# Menu
<#list entities as entity>
menu_${toUnderscoreCase(entity.@name)?lower_case}=${entity.@name}
</#list>
# Menu

# footer
# footer

# Common list
list_results_per_page=Résultats par page
list_results=Résultats de la recherche
list_from=de
list_to=à
list_page=page
list_display=Consulter
list_remove=Supprimer
list_edit=Éditer
list_search=Rechercher
list_result_empty=Aucun résultat ne correspond à vos filtres de recherche
# Common list

# Common form
form_save=Enregistrer
form_cancel=Annuler
form_new=Nouveau
form_create=Créer
form_update=Mettre à jour
form_delete=Supprimer
form_edit=Éditer
form_add=Ajouter
form_valid=Valider
yes=Oui
no=Non
select_all=Tous
blank_value=Non renseigné
select=Sélectionnez
back=Retour
next=Suivant
# Common form

# error messages
error_create_failed=La création a échoué
error_update_failed=La mise à jour a échoué
error_delete_failed=La suppression a échoué
error_unable_to_find_entity=Impossible de trouver l'entité
error_value_required={0} est obligatoire.
error_int_value_invalid={0} n''est pas un nombre entier valide.
error_float_value_invalid={0} n''est pas un nombre flottant valide.
error_date_value_invalid={0} n''est pas une date valide.
# error messages

# Home
home_head_title=Accueil
# Home


# Error
error_head_title=Erreur

error_exception_title=Erreur
error_exception_content=Une erreur est survenue, merci de contacter l'administrateur du site.

error_no_access_title=Accès interdit
error_no_access_content=Vous n'avez pas accès à cette page
# Error

<#list entities as entity>
<#include "/common/assign.inc" />
# ${entity.@name}
${toUnderscoreCase(entity.@name)?lower_case}_list_head_title=${entity.@name}
${toUnderscoreCase(entity.@name)?lower_case}_create_head_title=Création d'un ${entity.@name?lower_case}
${toUnderscoreCase(entity.@name)?lower_case}_update_head_title=Mise à jour de ${entity.@name?lower_case}
${toUnderscoreCase(entity.@name)?lower_case}_view_head_title=Détail de ${entity.@name?lower_case}

<#if primaryKey?node_name = "embedded-id">
<#if embeddedIdProperties??>
<#list embeddedIdProperties as property>
${toUnderscoreCase(entity.@name)?lower_case}_list_${toUnderscoreCase(property.@name)?lower_case}=${property.@name?cap_first}
</#list>
</#if>
<#else>
${toUnderscoreCase(entity.@name)?lower_case}_list_${toUnderscoreCase(primaryKey.@name)?lower_case}=${primaryKey.@name?cap_first}
</#if>
<#list allProperties as property>
${toUnderscoreCase(entity.@name)?lower_case}_list_${toUnderscoreCase(property.@name)?lower_case}=${property.@name?cap_first}
</#list>
${toUnderscoreCase(entity.@name)?lower_case}_list_found=${entity.@name?lower_case}(s) trouvée(s)
${toUnderscoreCase(entity.@name)?lower_case}_list_empty=Aucune ${entity.@name?lower_case} n'est enregistrée dans l'application

<#if primaryKey?node_name = "embedded-id">
<#if embeddedIdProperties??>
<#list embeddedIdProperties as property>
${toUnderscoreCase(entity.@name)?lower_case}_form_${toUnderscoreCase(property.@name)?lower_case}=${property.@name?cap_first}
</#list>
</#if>
<#else>
${toUnderscoreCase(entity.@name)?lower_case}_form_${toUnderscoreCase(primaryKey.@name)?lower_case}=${primaryKey.@name?cap_first}
</#if>
<#list allProperties as property>
${toUnderscoreCase(entity.@name)?lower_case}_form_${toUnderscoreCase(property.@name)?lower_case}=${property.@name?cap_first}
</#list>

<#if primaryKey?node_name = "embedded-id">
<#if embeddedIdProperties??>
<#list embeddedIdProperties as property>
error_${toUnderscoreCase(entity.@name)?lower_case}_${toUnderscoreCase(property.@name)?lower_case}_required=${property.@name?cap_first} est obligatoire.
</#list>
</#if>
</#if>
<#list allProperties as property>
<#if property?node_name == "column">
<#if getClassName(property.@type) != "Boolean" && getType(property.@type) != "boolean">
<#if util.xml.getAttribute(property.@nullable) == "false">
error_${toUnderscoreCase(entity.@name)?lower_case}_${toUnderscoreCase(property.@name)?lower_case}_required=${property.@name?cap_first} est obligatoire.
</#if>
</#if>
<#else>
<#if util.xml.getAttribute(property.@nullable) == "false">
error_${toUnderscoreCase(entity.@name)?lower_case}_${toUnderscoreCase(property.@name)?lower_case}_required=${property.@name?cap_first} est obligatoire.
</#if>
</#if>
</#list>
# ${entity.@name}

</#list>
