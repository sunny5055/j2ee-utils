#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.util;
import com.google.code.jee.utils.AbstractUnicodeResourceBundle;

public class ${resourceBundleClassName} extends AbstractUnicodeResourceBundle {
    
    /**
     * {@inheritedDoc}
     */
    @Override
    protected String getBundleName() {
        return "bundle.${resourceBundleName}";
    }

}