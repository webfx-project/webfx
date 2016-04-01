package naga.core.ngui.rx;

import javafx.beans.property.Property;
import naga.core.ngui.displayresult.DisplayResult;
import naga.core.orm.domainmodel.DomainModel;
import naga.core.orm.entity.EntityStore;
import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.ExpressionArray;
import naga.core.orm.expressionsqlcompiler.sql.SqlCompiled;
import naga.core.orm.filter.StringFilter;
import naga.core.orm.filter.StringFilterBuilder;
import naga.core.ngui.displayresult.DisplayColumn;
import naga.core.ngui.displayresult.EntityListToDisplayResultGenerator;
import naga.core.orm.mapping.SqlResultToEntityListGenerator;
import naga.core.spi.platform.Platform;
import naga.core.spi.sql.SqlArgument;
import naga.core.util.function.Converter;
import rx.Observable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class RxFilter {

    private final List<Observable<StringFilter>> stringFilterObservables = new ArrayList<>();
    private Object domainClassId;
    private DisplayColumn[] displayColumns;
    private DomainModel domainModel;
    private Object dataSourceId;
    private EntityStore store;
    private Object listId;

    public RxFilter setDomainModel(DomainModel domainModel) {
        this.domainModel = domainModel;
        return this;
    }

    public RxFilter setDataSourceId(Object dataSourceId) {
        this.dataSourceId = dataSourceId;
        return this;
    }

    public RxFilter setStore(EntityStore store) {
        this.store = store;
        return this;
    }

    public RxFilter setListId(Object listId) {
        this.listId = listId;
        return this;
    }

    public RxFilter setDisplayColumns(DisplayColumn... displayColumns) {
        this.displayColumns = displayColumns;
        return this;
    }

    public RxFilter combine(StringFilterBuilder stringFilterBuilder) {
        return combine(stringFilterBuilder.build());
    }

    public RxFilter combine(StringFilter stringFilter) {
        if (domainClassId == null)
            domainClassId = stringFilter.getDomainClassId();
        return combine(Observable.just(stringFilter));
    }

    public RxFilter combine(Observable<StringFilter> stringFilterObservable) {
        stringFilterObservables.add(stringFilterObservable);
        return this;
    }

    public RxFilter combine(Property<String> textProperty, Converter<String, String> inputTextToConditionConverter) {
        return combine(RxUi.observe(textProperty)
                .map(text -> text == null || text.isEmpty() ? null :
                        new StringFilterBuilder()
                                .setCondition(inputTextToConditionConverter.convert(text))
                                .build()
                ));
    }

    public RxFilter combine(Property<Boolean> ifProperty, StringFilterBuilder stringFilterBuilder) {
        return combine(RxUi.observeIf(Observable.just(stringFilterBuilder.build()), ifProperty));
    }

    private void checkFields() {
        if (store == null)
            store = new EntityStore();
        if (listId == null)
            listId = "default";
        List<Expression> displayPersistentTerms = new ArrayList<>();
        for (DisplayColumn displayColumn : displayColumns) {
            displayColumn.parseIfNecessary(domainModel, domainClassId);
            displayColumn.getExpression().collectPersistentTerms(displayPersistentTerms);
        }
        if (!displayPersistentTerms.isEmpty())
            combine(new StringFilterBuilder().setFields(new ExpressionArray<>(displayPersistentTerms).toString()));
    }

    public void displayResultInto(Property<DisplayResult> displayResultProperty) {
        checkFields();
        Observable<DisplayResult> displayResultObservable = Observable
                .combineLatest(stringFilterObservables, StringFilterBuilder::mergeStringFilters)
                .distinctUntilChanged()
                .switchMap(stringFilter -> {
                    SqlCompiled sqlCompiled = domainModel.compileSelect(stringFilter.toStringSelect());
                    Platform.log(sqlCompiled.getSql());
                    return RxFuture.from(Platform.sql().read(new SqlArgument(sqlCompiled.getSql(), dataSourceId)))
                            .map(sqlReadResult -> SqlResultToEntityListGenerator.createEntityList(sqlReadResult, sqlCompiled.getQueryMapping(), store, listId))
                            .map(entities -> EntityListToDisplayResultGenerator.createDisplayResult(entities, displayColumns));
                });
        RxUi.displayObservable(displayResultObservable, displayResultProperty);
    }

}

