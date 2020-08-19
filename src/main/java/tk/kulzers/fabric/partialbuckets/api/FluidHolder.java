package tk.kulzers.fabric.partialbuckets.api;

import net.minecraft.fluid.Fluid;
import java.util.Map;

@SuppressWarnings("unused")
abstract public class FluidHolder {
    protected Map<Class<? extends Fluid>, Float> maxFluid;
    protected Map<Class<? extends Fluid>, Float> curFluid;
    protected float maxTotalFluid = -1;
    protected float curTotalFluid = 0;
    protected float defaultMaxFluid = 0;
    public float fillFluid(Class<? extends Fluid> fluid, float amt) {
        float maxFluid = this.getMaxFluid(fluid);
        float curFluid = this.curFluid.getOrDefault(fluid, (float) 0);
        float newFluid = curFluid + amt;
        float overflow = 0;
        if (newFluid > maxFluid && maxFluid != -1) {
            overflow += (maxFluid - newFluid);
            newFluid = maxFluid;
        }
        if (newFluid < 0) {
            overflow += newFluid;
            newFluid = 0;
        }

        amt = newFluid - curFluid;
        float newTotalFluid = curTotalFluid + amt;
        if (newTotalFluid > maxTotalFluid) {
            overflow += (maxTotalFluid - newTotalFluid);
            newTotalFluid = maxTotalFluid;
        }
        if (newTotalFluid < 0) {
            overflow += newTotalFluid;
            newTotalFluid = 0;
        }

        amt = newTotalFluid - curTotalFluid;

        this.curFluid.put(fluid, curFluid + amt);
        this.curTotalFluid = newTotalFluid;
        return overflow;
    }

    /**
     * Look up the maximum fill of the given fluid.
     * @param fluid The class of the fluid to get the maximum fill for.
     * @return The maximum fill of the given fluid.
     */
    public float getMaxFluid(Class<? extends Fluid> fluid) {
        return this.maxFluid.getOrDefault(fluid, this.defaultMaxFluid);
    }
}
