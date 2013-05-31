<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "/common/common.inc" as util />
<@dropOutputFile />
<#assign entities = xml["//j:entity"]/>
<#assign projectName = xml["//p:configuration/p:projectName"]/>

<@resolveKey map=config key="resourceBundleFilePath" values=[projectName] assignTo="filePath"/>
<@resolveKey map=config key="resourceBundleFileName" values=[projectName] assignTo="fileName"/>
<@changeOutputFile name=filePath + "/"+ fileName />
# header
global_head_title=${projectName?cap_first}
# header

# Menu
<#list entities as entity>
menu_${toUnderscoreCase(entity.@name)?lower_case}=${entity.@name}
</#list>
# Menu

# footer
# footer

# Buttons
login_btn=Se connecter
logout_btn=Se déconnecter
save_btn=Enregistrer
cancel_btn=Annuler
new_btn=Nouveau
create_btn=Créer
update_btn=Mettre à jour
delete_btn=Supprimer
view_btn=Visualiser
edit_btn=Éditer
modify_btn=Modifier
add_btn=Ajouter
upload_btn=Charger
download_btn=Télécharger
return_btn=Retour
back_btn=Précédent
next_btn=Suivant
search_btn=Rechercher
reinit_btn=Réinitialiser
export_btn=Exporter
print_btn=Imprimer
# Buttons

loading=Chargement...

# Common list
list_results_per_page=Résultats par page
list_results=Résultats de la recherche
list_from=de
list_to=à
list_page=page
list_search_title=Recherche
list_advanced_search_title=Recherche avancée
list_simple_search_title=Recherche simple
list_result_empty=Aucun résultat ne correspond à vos filtres de recherche
# Common list

# Common form
yes=Oui
no=Non
select_all=Tous
blank_value=Non renseigné
select=Sélectionnez
confirm_delete_title=Suppression
confirm_delete_content=Êtes-vous sûr de vouloir supprimer cet élément ?
# Common form

# error messages
error_create_failed=La création a échoué
error_update_failed=La mise à jour a échoué
error_delete_failed=La suppression a échoué
error_unable_to_delete=La suppression est impossible
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

<#list allProperties as property>
${toUnderscoreCase(entity.@name)?lower_case}_filter_${toUnderscoreCase(property.@name)?lower_case}=${property.@name?cap_first}
</#list>

${toUnderscoreCase(entity.@name)?lower_case}_list_${toUnderscoreCase(primaryKey.@name)?lower_case}=${primaryKey.@name?cap_first}
<#list allProperties as property>
${toUnderscoreCase(entity.@name)?lower_case}_list_${toUnderscoreCase(property.@name)?lower_case}=${property.@name?cap_first}
</#list>
${toUnderscoreCase(entity.@name)?lower_case}_list_found=${entity.@name?lower_case}(s) trouvée(s)
${toUnderscoreCase(entity.@name)?lower_case}_list_empty=Aucune ${entity.@name?lower_case} n'est enregistrée dans l'application

${toUnderscoreCase(entity.@name)?lower_case}_created=${entity.@name} a été créé avec succès
${toUnderscoreCase(entity.@name)?lower_case}_updated=${entity.@name} a été mis à jour avec succès
${toUnderscoreCase(entity.@name)?lower_case}_deleted=${entity.@name} a été supprimé avec succès
${toUnderscoreCase(entity.@name)?lower_case}_form_${toUnderscoreCase(primaryKey.@name)?lower_case}=${primaryKey.@name?cap_first}
<#list allProperties as property>
${toUnderscoreCase(entity.@name)?lower_case}_form_${toUnderscoreCase(property.@name)?lower_case}=${property.@name?cap_first}
</#list>

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
