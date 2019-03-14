package mongoose.shared.entities.impl;

import mongoose.shared.entities.SystemMetricsEntity;
import webfx.framework.shared.orm.entity.EntityId;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.framework.shared.orm.entity.impl.DynamicEntity;
import webfx.framework.shared.orm.entity.impl.EntityFactoryProviderImpl;

import java.time.Instant;

/**
 * @author Bruno Salmon
 */
public final class SystemMetricsEntityImpl extends DynamicEntity implements SystemMetricsEntity {

    public SystemMetricsEntityImpl(EntityId id, EntityStore store) {
        super(id, store);
    }

    @Override
    public void setDate(Instant date) {
        setFieldValue("date", date);
    }

    @Override
    public Instant getDate() {
        return getInstantFieldValue("date");
    }

    @Override
    public void setMemoryTotal(Long memoryTotal) {
        setFieldValue("memoryTotal", memoryTotal);
    }

    @Override
    public Long getMemoryTotal() {
        return getLongFieldValue("memoryTotal");
    }

    @Override
    public void setMemoryFree(Long memoryFree) {
        setFieldValue("memoryFree", memoryFree);
    }

    @Override
    public Long getMemoryFree() {
        return getLongFieldValue("memoryFree");
    }

    @Override
    public void setMemoryMax(Long memoryMax) {
        setFieldValue("memoryMax", memoryMax);
    }

    @Override
    public Long getMemoryMax() {
        return getLongFieldValue("memoryMax");
    }

    @Override
    public void setSystemLoadAverage(Double systemLoadAverage) {
        setFieldValue("systemLoadAverage", systemLoadAverage);
    }

    @Override
    public Double getSystemLoadAverage() {
        return getDoubleFieldValue("systemLoadAverage");
    }

    @Override
    public void setProcessCpuLoad(Double processCpuLoad) {
        setFieldValue("processCpuLoad", processCpuLoad);
    }

    @Override
    public Double getProcessCpuLoad() {
        return getDoubleFieldValue("processCpuLoad");
    }

    @Override
    public Long getMemoryUsed() {
        return getLongFieldValue("memoryUsed");
    }

    public static final class ProvidedFactory extends EntityFactoryProviderImpl<SystemMetricsEntity> {
        public ProvidedFactory() {
            super(SystemMetricsEntity.class, "Metrics", SystemMetricsEntityImpl::new);
        }
    }
}
