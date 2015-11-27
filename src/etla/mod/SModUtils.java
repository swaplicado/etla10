/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod;

import sa.lib.SLibConsts;
import sa.lib.gui.SGuiModuleUtils;

/**
 *
 * @author Sergio Flores
 */
public class SModUtils implements SGuiModuleUtils {

    public SModUtils() {

    }

    @Override
    public int getModuleTypeByType(final int type) {
        int module = SLibConsts.UNDEFINED;

        if (type >= SModConsts.MOD_CFG && type < SModConsts.MOD_ETL) {
            module = SModConsts.MOD_CFG;
        }
        else if (type >= SModConsts.MOD_ETL) {
            module = SModConsts.MOD_ETL;
        }

        return module;
    }

    @Override
    public int getModuleSubtypeBySubtype(final int type, final int subtype) {
        return SLibConsts.UNDEFINED;
    }
}
