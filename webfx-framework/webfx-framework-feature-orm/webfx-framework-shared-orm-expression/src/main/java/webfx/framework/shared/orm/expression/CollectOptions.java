package webfx.framework.shared.orm.expression;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class CollectOptions {

    private final List<Expression> collectedTerms;
    // TODO Make this class immutable and create a builder
    private boolean distinct = true;
    private boolean filterPersistentTerms;
    private boolean factorizeLeftDot;
    private boolean traverseSqlExpressible;
    private boolean includeParameter;
    private boolean traverseSelect;

    public CollectOptions() {
        this(new ArrayList<>());
    }

    public CollectOptions(List<Expression> collectedTerms) {
        this.collectedTerms = collectedTerms;
    }

    public CollectOptions applyOptions(CollectOptions options) {
        distinct = options.distinct;
        filterPersistentTerms = options.filterPersistentTerms;
        factorizeLeftDot = options.factorizeLeftDot;
        traverseSqlExpressible = options.traverseSqlExpressible;
        includeParameter = options.includeParameter;
        traverseSelect = options.traverseSelect;
        return this;
    }

    public CollectOptions setFilterPersistentTerms(boolean filterPersistentTerms) {
        this.filterPersistentTerms = filterPersistentTerms;
        return this;
    }

    public CollectOptions setFactorizeLeftDot(boolean factorizeLeftDot) {
        this.factorizeLeftDot = factorizeLeftDot;
        return this;
    }

    public CollectOptions setTraverseSqlExpressible(boolean traverseSqlExpressible) {
        this.traverseSqlExpressible = traverseSqlExpressible;
        return this;
    }

    public CollectOptions setIncludeParameter(boolean includeParameter) {
        this.includeParameter = includeParameter;
        return this;
    }

    public CollectOptions setTraverseSelect(boolean traverseSelect) {
        this.traverseSelect = traverseSelect;
        return this;
    }

    public boolean filterPersistentTerms() {
        return filterPersistentTerms;
    }

    public boolean traverseAs() {
        return factorizeLeftDot;
    }

    public boolean traverseSqlExpressible() {
        return traverseSqlExpressible;
    }

    public boolean includeParameter() {
        return includeParameter;
    }

    public boolean traverseSelect() {
        return traverseSelect;
    }

    public boolean factorizeLeftDot() {
        return factorizeLeftDot;
    }

    public <T> List<Expression<T>> getCollectedTerms() {
        return (List<Expression<T>>) (List) collectedTerms;
    }

    public void addTerm(Expression term) {
        if (!distinct || !collectedTerms.contains(term))
            collectedTerms.add(term);
    }

    public static CollectOptions persistentTermsOnly(List<Expression> persistentTerms) {
        return new CollectOptions(persistentTerms)
                .setFilterPersistentTerms(true)
                .setIncludeParameter(true)
                .setFactorizeLeftDot(true)
                ;
    }

    public static CollectOptions sameButEmpty(CollectOptions options) {
        return new CollectOptions().applyOptions(options);
    }
}
