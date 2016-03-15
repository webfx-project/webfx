package naga.core.rx;

import naga.core.ngui.displayresult.DisplayResult;
import naga.core.orm.domainmodel.DomainModel;
import naga.core.orm.entity.EntityStore;
import naga.core.orm.expressionsqlcompiler.sql.SqlCompiled;
import naga.core.orm.filter.StringFilter;
import naga.core.orm.filter.StringFilterBuilder;
import naga.core.orm.mapping.display.EntityListToDisplayResultGenerator;
import naga.core.orm.mapping.query.SqlResultToEntityListGenerator;
import naga.core.spi.gui.node.DisplayNode;
import naga.core.spi.gui.node.UserInputNode;
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

    public RxFilter combine(StringFilterBuilder stringFilterBuilder) {
        return combine(stringFilterBuilder.build());
    }

    public RxFilter combine(StringFilter stringFilter) {
        return combine(Observable.just(stringFilter));
    }

    public RxFilter combine(Observable<StringFilter> stringFilterObservable) {
        stringFilterObservables.add(stringFilterObservable);
        return this;
    }

    public RxFilter combine(UserInputNode<String, ?> searchBox, Converter<String, String> inputToConditionConverter) {
        return combine(RxUi.observe(searchBox)
                .map(s -> s == null || s.isEmpty() ? null :
                        new StringFilterBuilder()
                        .setCondition(inputToConditionConverter.convert(s))
                        .build()
                ));
    }

    public RxFilter combine(UserInputNode<Boolean, ?> checkBox, StringFilterBuilder stringFilterBuilder) {
        return combine(RxUi.observeIf(Observable.just(stringFilterBuilder.build()), checkBox));
    }

    public void displayResultInto(DisplayNode displayNode) {
        if (store == null)
            store = new EntityStore();
        if (listId == null)
            listId = "test";
        Observable<DisplayResult> displayResultObservable = combineStringFilters(stringFilterObservables).switchMap(stringFilter -> {
            SqlCompiled sqlCompiled = domainModel.compileSelect(stringFilter.toStringSelect());
            Platform.log(sqlCompiled.getSql());
            return RxFuture.from(Platform.sql().read(new SqlArgument(sqlCompiled.getSql(), dataSourceId)))
                    .map(sqlReadResult -> SqlResultToEntityListGenerator.createEntityList(sqlReadResult, sqlCompiled.getQueryMapping(), store, listId))
                    .map(entities -> EntityListToDisplayResultGenerator.createDisplayResult(entities, stringFilter.getDisplayFields(), domainModel, stringFilter.getDomainClassId()));
        });
        RxUi.displayObservable(displayResultObservable, displayNode);
    }

    private static Observable<StringFilter> combineStringFilters(List<Observable<StringFilter>> stringFilterObservables) {
        return Observable.combineLatest(stringFilterObservables,
                StringFilterBuilder::mergeStringFilters)
                .distinctUntilChanged();
    }

}

