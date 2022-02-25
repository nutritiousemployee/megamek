/*
 * MegaMek - Copyright (C) 2004, 2005 Ben Mazur (bmazur@sev.org)
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 */
package megamek.common.weapons.infantry;

import megamek.common.AmmoType;

/**
 * @author Ben Grills
 * @since Sep 7, 2005
 */
public class InfantryArchaicBladeZweihanderSwordWeapon extends InfantryWeapon {
    private static final long serialVersionUID = -3164871600230559641L;

    public InfantryArchaicBladeZweihanderSwordWeapon() {
        super();

        name = "Blade (Zweihander Sword)";
        setInternalName(name);
        addLookupName("InfantryZweihanderSword");
        addLookupName("ZweihanderSword");
        ammoType = AmmoType.T_NA;
        cost = 60;
        tonnage = 0.0051;
        bv = 0.0;
        flags = flags.or(F_NO_FIRES).or(F_INF_POINT_BLANK).or(F_INF_ARCHAIC);
        infantryDamage = 0.05;
        infantryRange = 0;
        rulesRefs = "195, ATOW-C";
        techAdvancement.setTechBase(TECH_BASE_ALL).setISAdvancement(1950, 1950, 1950, DATE_NONE, DATE_NONE)
                .setISApproximate(false, false, false, false, false)
                .setClanAdvancement(1950, 1950, 1950, DATE_NONE, DATE_NONE)
                .setClanApproximate(false, false, false, false, false).setTechRating(RATING_A)
                .setAvailability(RATING_A, RATING_A, RATING_A, RATING_B);

    }
}
