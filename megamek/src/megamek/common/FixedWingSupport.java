/*
 * MegaAero - Copyright (C) 2010 Jason Tighe This program is free software; you
 * can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 */
/*
 * Created on 10/31/2010
 */
package megamek.common;

import java.util.Map;

/**
 * @author Jason Tighe
 */
public class FixedWingSupport extends ConvFighter {


    /**
     *
     */
    private static final long serialVersionUID = 347113432982248518L;


    public static final int LOC_BODY = 5;

    private static String[] LOCATION_ABBRS =
        { "NOS", "LWG", "RWG", "AFT", "WNG", "BOD" };
    private static String[] LOCATION_NAMES =
        { "Nose", "Left Wing", "Right Wing", "Aft", "Wings", "Body" };
    private int[] barRating;
    /** Vehicles can be constructed with seating for additional crew. This has no effect on play */
    private int extraCrewSeats = 0;

    public FixedWingSupport() {
        super();
        damThresh = new int[] { 0, 0, 0, 0, 0, 0 };
        barRating = new int[locations()];
    }

    public void setBARRating(int rating, int loc) {
        barRating[loc] = rating;
    }

    @Override
    public void setBARRating(int rating) {
        for (int i = 0; i < locations(); i++) {
            barRating[i] = rating;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see megamek.common.Entity#getBARRating()
     */
    @Override
    public int getBARRating(int loc) {
        return barRating[loc];
    }

    /*
     * (non-Javadoc)
     *
     * @see megamek.common.Entity#hasBARArmor()
     */
    @Override
    public boolean hasBARArmor(int loc) {
        return true;
    }

    @Override
    public String[] getLocationAbbrs() {
        return LOCATION_ABBRS;
    }

    @Override
    public String[] getLocationNames() {
        return LOCATION_NAMES;
    }

    @Override
    public int locations() {
        return 6;
    }

    @Override
    public boolean isSupportVehicle() {
        return true;
    }

    @Override
    public void autoSetSI() {
        initializeSI(getOriginalWalkMP());
    }

    @Override
    public boolean isVSTOL() {
        return hasWorkingMisc(MiscType.F_VSTOL_CHASSIS);
    }

    @Override
    public boolean isSTOL() {
        return hasWorkingMisc(MiscType.F_STOL_CHASSIS);
    }

    public boolean hasPropChassisMod() {
        return hasWorkingMisc(MiscType.F_PROP);
    }

    /**
     * The mass of each point of fuel in kg, based on weight class and engine tech rating.
     */
    private static final int[][] KG_PER_FUEL_POINT = {
            {50, 30, 23, 15, 13, 10}, // small
            {63, 38, 25, 20, 18, 15}, // medium
            {83, 50, 35, 28, 23, 20} // large
    };

    /**
     * While most aerospace units measure fuel weight in points per ton, support vehicles measure
     * in kg per point. Vehicles that do not require fuel return 0.
     *
     * @return The mass of each point of fuel in kg.
     */
    public int kgPerFuelPoint() {
        int kg = KG_PER_FUEL_POINT[getWeightClass() - EntityWeightClass.WEIGHT_SMALL_SUPPORT][getEngineTechRating()];
        if (hasPropChassisMod() || getMovementMode().equals(EntityMovementMode.AIRSHIP)) {
            if (getEngine().isFusion() || (getEngine().getEngineType() == Engine.FISSION)
                    || (getEngine().getEngineType() == Engine.SOLAR)) {
                return 0;
            }
            kg = (int) Math.ceil(kg * 0.75);
        }
        return kg;
    }

    @Override
    public double getFuelTonnage() {
        double weight = getOriginalFuel() * kgPerFuelPoint() / 1000.0;
        if (getWeightClass() != EntityWeightClass.WEIGHT_SMALL_SUPPORT) {
            weight = Math.ceil(weight * 2.0) * 0.5;
        }
        return weight;
    }

    @Override
    public double getFuelPointsPerTon() {
        return 1000.0 / kgPerFuelPoint();
    }

    protected static final TechAdvancement TA_FIXED_WING_SUPPORT = new TechAdvancement(TECH_BASE_ALL)
            .setAdvancement(DATE_PS, DATE_PS, DATE_PS)
            .setTechRating(RATING_B).setAvailability(RATING_C, RATING_D, RATING_C, RATING_C)
            .setStaticTechLevel(SimpleTechLevel.STANDARD);
    protected static final TechAdvancement TA_FIXED_WING_SUPPORT_LARGE = new TechAdvancement(TECH_BASE_ALL)
            .setAdvancement(DATE_PS, DATE_PS, DATE_PS)
            .setTechRating(RATING_B).setAvailability(RATING_D, RATING_E, RATING_D, RATING_D)
            .setStaticTechLevel(SimpleTechLevel.STANDARD);
    protected static final TechAdvancement TA_AIRSHIP_SUPPORT_SMALL = new TechAdvancement(TECH_BASE_ALL)
            .setAdvancement(DATE_PS, DATE_PS, DATE_PS)
            .setTechRating(RATING_A).setAvailability(RATING_C, RATING_D, RATING_C, RATING_C)
            .setStaticTechLevel(SimpleTechLevel.STANDARD);
    protected static final TechAdvancement TA_AIRSHIP_SUPPORT_MEDIUM = new TechAdvancement(TECH_BASE_ALL)
            .setAdvancement(DATE_PS, DATE_PS, DATE_PS)
            .setTechRating(RATING_B).setAvailability(RATING_D, RATING_E, RATING_D, RATING_D)
            .setStaticTechLevel(SimpleTechLevel.STANDARD);
    // Availability missing from TO. Using medium
    protected static final TechAdvancement TA_AIRSHIP_SUPPORT_LARGE = new TechAdvancement(TECH_BASE_ALL)
            .setAdvancement(DATE_PS, DATE_PS, DATE_PS)
            .setTechRating(RATING_C).setAvailability(RATING_D, RATING_E, RATING_D, RATING_D)
            .setStaticTechLevel(SimpleTechLevel.ADVANCED);
    // Availability missing from TO. Using similar values from other support vees.
    // Also using early spaceflight for intro dates based on common sense.
    protected static final TechAdvancement TA_SATELLITE = new TechAdvancement(TECH_BASE_ALL)
            .setAdvancement(DATE_ES, DATE_ES, DATE_ES)
            .setTechRating(RATING_C).setAvailability(RATING_D, RATING_E, RATING_D, RATING_D)
            .setStaticTechLevel(SimpleTechLevel.ADVANCED);

    @Override
    public TechAdvancement getConstructionTechAdvancement() {
        return getConstructionTechAdvancement(getMovementMode(), getWeightClass());
    }

    public static TechAdvancement getConstructionTechAdvancement(EntityMovementMode movementMode, int weightClass) {
        if (movementMode.equals(EntityMovementMode.AIRSHIP)) {
            if (weightClass == EntityWeightClass.WEIGHT_LARGE_SUPPORT) {
                return TA_AIRSHIP_SUPPORT_LARGE;
            } else if (weightClass == EntityWeightClass.WEIGHT_MEDIUM_SUPPORT) {
                return TA_AIRSHIP_SUPPORT_MEDIUM;
            } else {
                return TA_AIRSHIP_SUPPORT_SMALL;
            }
        } else if (movementMode.equals(EntityMovementMode.STATION_KEEPING)) {
            return TA_SATELLITE;
        } else if (weightClass == EntityWeightClass.WEIGHT_LARGE_SUPPORT) {
            return TA_FIXED_WING_SUPPORT_LARGE;
        } else {
            return TA_FIXED_WING_SUPPORT;
        }
    }

    @Override
    public int getBattleForceSize() {
        //The tables are on page 356 of StartOps
        if (getWeight() < 5) {
            return 1;
        }
        if (getWeight() < 100) {
            return 2;
        }

        return 3;
    }

    @Override
    protected int calculateWalk() {
        return getOriginalWalkMP();
    }

    @Override
    public void autoSetMaxBombPoints() {
        // fixed wing support craft need external stores hardpoints to be able to carry bombs
        int bombpoints = 0;
        for (Mounted misc : getMisc()) {
            if (misc.getType().hasFlag(MiscType.F_EXTERNAL_STORES_HARDPOINT)) {
                bombpoints++;
            }
        }
        maxBombPoints = bombpoints;
    }

    @Override
    public void initializeThresh(int loc) {
        int bar = getBARRating(loc);
        if (bar == 10) {
            setThresh((int) Math.ceil(getArmor(loc) / 10.0), loc);
        } else if (bar >= 2) {
            setThresh(1, loc);
        } else {
            setThresh(0, loc);
        }
    }

    public double getBaseEngineValue() {
        if (getWeight() < 5) {
            return 0.005;
        } else if (getWeight() <= 100) {
            return 0.01;
        } else {
            return 0.015;
        }
    }

    public double getBaseChassisValue() {
        if (getWeight() < 5) {
            return 0.08;
        } else if (getWeight() <= 100) {
            return 0.1;
        } else {
            return 0.15;
        }
    }

    public int getTotalSlots() {
        return 5 + (int) Math.floor(getWeight() / 10);
    }

    /**
     * @return Additional seats beyond the minimum crew requirements
     */
    public int getExtraCrewSeats() {
        return extraCrewSeats;
    }

    public void setExtraCrewSeats(int seats) {
        this.extraCrewSeats = seats;
    }
    
    @Override
    public void addBattleForceSpecialAbilities(Map<BattleForceSPA,Integer> specialAbilities) {
        super.addBattleForceSpecialAbilities(specialAbilities);
        specialAbilities.put(BattleForceSPA.ATMO, null);
        if (getMaxBombPoints() > 0) {
            specialAbilities.put(BattleForceSPA.BOMB, getMaxBombPoints() / 5);
        }
    }

    public long getEntityType(){
        return Entity.ETYPE_AERO | Entity.ETYPE_CONV_FIGHTER | Entity.ETYPE_FIXED_WING_SUPPORT;
    }
}