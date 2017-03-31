/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.aspectj.options;

import org.netbeans.spi.options.AdvancedOption;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.NbBundle;

public final class AspectjfornetbeansmoduleAdvancedOption extends AdvancedOption {

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(AspectjfornetbeansmoduleAdvancedOption.class, "AdvancedOption_DisplayName_Aspectjfornetbeansmodule");
    }

    @Override
    public String getTooltip() {
        return NbBundle.getMessage(AspectjfornetbeansmoduleAdvancedOption.class, "AdvancedOption_Tooltip_Aspectjfornetbeansmodule");
    }

    @Override
    public OptionsPanelController create() {
        return new AspectjfornetbeansmoduleOptionsPanelController();
    }
}
