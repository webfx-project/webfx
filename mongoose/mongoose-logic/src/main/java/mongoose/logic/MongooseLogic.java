package mongoose.logic;

import mongoose.domainmodel.DomainModelSnapshotLoader;
import naga.core.Naga;
import naga.core.buscall.BusCallService;
import naga.core.jsoncodec.JsonCodecManager;
import naga.core.ngui.displayresult.DisplayResult;
import naga.core.orm.domainmodel.DomainModel;
import naga.core.orm.entity.EntityList;
import naga.core.orm.entity.EntityStore;
import naga.core.orm.expression.term.Constant;
import naga.core.orm.expression.term.Plus;
import naga.core.orm.expression.term.Select;
import naga.core.orm.expressionparser.ExpressionParser;
import naga.core.orm.expressionparser.lci.ParserDomainModelReaderMock;
import naga.core.orm.expressionsqlcompiler.ExpressionSqlCompiler;
import naga.core.orm.expressionsqlcompiler.lci.CompilerDomainModelReaderMock;
import naga.core.orm.expressionsqlcompiler.sql.DbmsSqlSyntaxOptions;
import naga.core.orm.expressionsqlcompiler.sql.SqlCompiled;
import naga.core.orm.filter.StringFilter;
import naga.core.orm.filter.StringFilterBuilder;
import naga.core.orm.mapping.display.EntityListToDisplayResultGenerator;
import naga.core.orm.mapping.query.SqlColumnToEntityFieldMapping;
import naga.core.orm.mapping.query.SqlResultToEntityListGenerator;
import naga.core.orm.mapping.query.SqlRowToEntityMapping;
import naga.core.rx.FutureSource;
import naga.core.rx.PropertySource;
import naga.core.spi.gui.GuiToolkit;
import naga.core.spi.gui.node.BorderPane;
import naga.core.spi.gui.node.SearchBox;
import naga.core.spi.gui.node.Table;
import naga.core.spi.gui.node.ToggleButton;
import naga.core.spi.platform.Platform;
import naga.core.spi.sql.SqlArgument;
import naga.core.spi.sql.SqlReadResult;
import naga.core.util.async.Future;
import rx.Observable;

import java.util.Arrays;

/**
 * @author Bruno Salmon
 */
public class MongooseLogic {

    public static void runBackendApplication() {
        //testBase64();
        //testJavaFastPFOR();
        //runExpressionDomainModelDemo();
        runFilterGuiDemo();
    }

    private static void runFilterGuiDemo() {
        GuiToolkit toolkit = GuiToolkit.get();
        Table table = toolkit.createNode(Table.class);
        ToggleButton limitCheckBox = toolkit.createNode(ToggleButton.class);
        limitCheckBox.setText("Limit to 100");
        limitCheckBox.setSelected(true);
        SearchBox searchBox = toolkit.createNode(SearchBox.class);

        BorderPane borderPane = toolkit.createNode(BorderPane.class);
        borderPane.setTop(searchBox);
        borderPane.setCenter(table);
        borderPane.setBottom(limitCheckBox);

        toolkit.setRootNode(borderPane);

        //new Thread(()->{
            toolkit.observeAndDisplay(MongooseLogic.getFilterDemoDisplayResultObservable(PropertySource.fromObervableValue(searchBox.getUserInputProperty()), PropertySource.fromObervableValue(limitCheckBox.getUserInputProperty())), table);
        //}).start();
        searchBox.requestFocus();
    }

    /*private static void runFilterDemo() {
        Platform.log("***********************");
        Platform.log("***** RxJava TEST *****");
        Platform.log("***********************");
        Platform.log("");
        Observable<DisplayResult> displayResultObservable = getFilterDemoDisplayResultObservable();

        displayResultObservable.subscribe(Platform::log);

    }*/

    /*public static Observable<DisplayResult> getFilterDemoDisplayResultObservable() {
        BehaviorSubject<Boolean> limitCheckedObservable = BehaviorSubject.create(false); // Observable.interval(0, 1, TimeUnit.SECONDS).flatMap(time -> Observable.just(time % 2 == 0));
        Random random = new Random();
        Platform.schedulePeriodic(1_000, event -> {
            boolean checkbox = random.nextBoolean();
            Platform.log("Simulating limit checkbox: " + checkbox);
            limitCheckedObservable.onNext(checkbox);
        });
        return getFilterDemoDisplayResultObservable(limitCheckedObservable);
    }*/

    public static Observable<DisplayResult> getFilterDemoDisplayResultObservable(Observable<String> searchTextObservable, Observable<Boolean> limitCheckedObservable) {
        DomainModel domainModel = DomainModelSnapshotLoader.loadDomainModelFromSnapshot();
        EntityStore store = new EntityStore();
        Object listId = "test";


        Observable<StringFilter> baseFilterObservable = Observable.just(new StringFilterBuilder("Organization")
                .setOrderBy("name")
                .setLogicFields("country.(code,continent.code)")
                .build()
        );
        Observable<StringFilter> fieldsObservable = Observable.just(new StringFilterBuilder("Organization")
                .setDisplayFields("name,type.name, country.(name + ' (' + continent.name + ')')")
                .build()
        );
        Observable<StringFilter> conditionObservable = Observable.just(new StringFilterBuilder("Organization")
                .setCondition("!closed")
                .build()
        );
        Observable<StringFilter> searchObservable = searchTextObservable.map(s -> s == null || s.isEmpty() ? null : new StringFilterBuilder("Organization").setCondition("lower(name) like '%" + s.toLowerCase() + "%'").build());

        Observable<StringFilter> limitObservable = Observable.combineLatest(limitCheckedObservable,
                Observable.just(new StringFilterBuilder("Organization")
                        .setLimit("100")
                        .build()
                ), (limitChecked, limitFilter) -> limitChecked ? limitFilter : null);

        Observable<StringFilter> stringFilterObservable = Observable.combineLatest(
                Arrays.asList(baseFilterObservable, fieldsObservable, conditionObservable, searchObservable, limitObservable),
                StringFilterBuilder::mergeStringFilters)
                .distinctUntilChanged();

        Observable<DisplayResult> displayResultObservable = stringFilterToDisplayResultObservable(stringFilterObservable, domainModel, store, listId);

        return displayResultObservable;
    }

    private static Observable<DisplayResult> stringFilterToDisplayResultObservable(Observable<StringFilter> stringFilterObservable, DomainModel domainModel, EntityStore store, Object listId) {
        return stringFilterObservable.switchMap(stringFilter -> {
            SqlCompiled sqlCompiled = domainModel.compileSelect(stringFilter.toStringSelect());
            Platform.log(sqlCompiled.getSql());
            return FutureSource.fromFuture(Platform.sql().read(new SqlArgument(sqlCompiled.getSql(), 3)))
                    .map(sqlReadResult -> SqlResultToEntityListGenerator.createEntityList(sqlReadResult, sqlCompiled.getQueryMapping(), store, listId))
                    .map(entities -> EntityListToDisplayResultGenerator.createDisplayResult(entities, stringFilter.getDisplayFields(), domainModel, stringFilter.getDomainClassId()));
        });
    }

    private static void runSqlDemo() {
        Platform.sql().read(new SqlArgument("select id,name from organization order by name", 3)).setHandler(asyncResult -> Platform.log(asyncResult.succeeded() ? "Result : " + JsonCodecManager.encodeToJsonObject(asyncResult.result()).toJsonString() : "Error: " + asyncResult.cause()));
    }

    private static void runExpressionDemo() {
        Object result = new Plus<>(Constant.newConstant(5), Constant.newConstant(10)).evaluate(null, null);
        Platform.log(result);
    }

    private static void runExpressionParserDemo() {
        Select select = ExpressionParser.parseSelect("select event,<ident> from Document order by id desc limit 100",
                new ParserDomainModelReaderMock()
                        .setFieldGroup("ident", "ref,person_firstName,person_lastName,organization.name"));
        Platform.log("expression: " + select);

        SqlCompiled sqlCompiled = ExpressionSqlCompiler.compileSelect(select, null, DbmsSqlSyntaxOptions.POSTGRES_SYNTAX, true, true,
                new CompilerDomainModelReaderMock()
                        .declareAsForeignField("Document", "event")
                        .setDomainClassForeignFields("Event", "name,startDate"));
        Platform.log("sql: " + sqlCompiled.getSql());

        Platform.sql().read(new SqlArgument(sqlCompiled.getSql(), 3)).setHandler(asyncResult ->
                Platform.log(SqlResultToEntityListGenerator.createEntityList(asyncResult.result(), sqlCompiled.getQueryMapping(), new EntityStore(), "test")));
    }

    private static void runExpressionDomainModelDemo() {
        Platform.log("**********************");
        Platform.log("***** COLD TESTS *****");
        Platform.log("**********************");
        Platform.log("");
        testExpressionDomainModel().setHandler(event -> {
            Platform.log("");
            Platform.log("**********************");
            Platform.log("***** WARM TESTS *****");
            Platform.log("**********************");
            Platform.log("");
            /*testExpressionDomainModel().setHandler(
                    event1 -> runFilterDemo()
            );*/
        });
    }

    private static Future<Void> testExpressionDomainModel() {
        Platform.log("1) Loading KBS2 domain model in memory");
        long t0 = System.currentTimeMillis();
        DomainModel domainModel = DomainModelSnapshotLoader.loadDomainModelFromSnapshot();
        Platform.log("Done in " + (System.currentTimeMillis() - t0) + " ms");

        Platform.log("2) Parsing EQL select");
        t0 = System.currentTimeMillis();
        String eql = "select name,country.(name, continent) from Organization where !closed && type.name!='Branch' order by name";
        Platform.log("Parsing expression: " + eql);
        Select select = domainModel.parseSelect(eql);
        Platform.log("Done in " + (System.currentTimeMillis() - t0) + " ms");
        Platform.log("Expression in memory: " + select);

        Platform.log("3) Compiling expression to SQL");
        t0 = System.currentTimeMillis();
        SqlCompiled sqlCompiled = domainModel.compileSelect(select);
        Platform.log("Done in " + (System.currentTimeMillis() - t0) + " ms");
        Platform.log("Generated SQL: " + sqlCompiled.getSql());
        Platform.log("Generated query mapping: " + sqlCompiled.getQueryMapping());

        Platform.log("4) Establishing json event bus websocket connection");
        long t1 = System.currentTimeMillis();

        Future<Void> testFuture = Future.future();
        BusCallService.call(Naga.VERSION_ADDRESS, "ignored").setHandler(asyncVersionResult -> {
            if (asyncVersionResult.failed()) {
                Platform.log("Failed, cause: " + asyncVersionResult.cause());
                testFuture.fail(asyncVersionResult.cause());
            } else {
                Platform.log("Done in " + (System.currentTimeMillis() - t1) + " ms");

                Platform.log("5) Executing SQL");
                long t2 = System.currentTimeMillis();
                Platform.sql().read(new SqlArgument(sqlCompiled.getSql(), 3)).setHandler(asyncSqlResult -> {
                    if (asyncSqlResult.failed()) {
                        Platform.log("Failed, cause: " + asyncSqlResult.cause());
                        testFuture.fail(asyncVersionResult.cause());
                    } else {
                        Platform.log("Done in " + (System.currentTimeMillis() - t2) + " ms");
                        Platform.log("Number of rows: " + asyncSqlResult.result().getRowCount());
                        Platform.log("SQL result (in JSON): " + JsonCodecManager.encodeToJsonObject(asyncSqlResult.result()).toJsonString());

                        Platform.log("6) Generating entities");
                        long t3 = System.currentTimeMillis();
                        EntityStore store = new EntityStore();
                        EntityList entities = SqlResultToEntityListGenerator.createEntityList(asyncSqlResult.result(), sqlCompiled.getQueryMapping(), store, "test");
                        Platform.log("Done in " + (System.currentTimeMillis() - t3) + " ms");
                        Platform.log("Entity store contains " + store.getEntityClassesCountReport());
                        Platform.log("Entities: " + entities);

                        Platform.log("7) Generating display result");
                        t3 = System.currentTimeMillis();
                        DisplayResult displayResult = EntityListToDisplayResultGenerator.createDisplayResult(entities, "name, country.(name + ' (' + continent.name + ')')", domainModel, select.getDomainClass());
                        Platform.log("Done in " + (System.currentTimeMillis() - t3) + " ms");
                        Platform.log("Display result: " + displayResult);
                        testFuture.complete();
                    }
                });
            }
        });
        return testFuture;
    }


    public static void runQueryMappingDemo() {
        Future<SqlReadResult> future = Platform.sql().read(new SqlArgument(
                "select " +
                        "o.id," +    // Column 0
                        "o.name," +  // Column 1
                        "cy.id," +   // Column 2
                        "cy.name," + // Column 3
                        "ct.id," +   // Column 4
                        "ct.name " + // Column 5
                        "from organization o join country cy on cy.id=o.country_id join continent ct on ct.id=cy.continent_id " +
                        "order by o.name", 3));

        SqlColumnToEntityFieldMapping countryIdMapping, continentIdMapping;
        SqlRowToEntityMapping mapping = new SqlRowToEntityMapping(0 /* identity column is 0 (o.id) */, "Organization",
                naga.core.util.Arrays.toArray(
                        // column 1 (o.name): => simple mapping to primary entity Organization "name" field
                        new SqlColumnToEntityFieldMapping(1, "name"),
                        // column 2 (cy.id): mapping to primary entity Organization "country" foreign field
                        countryIdMapping = new SqlColumnToEntityFieldMapping(2, "country", "Country"),
                        // Column 3 (cy.name): simple mapping to foreign entity Country "name" field
                        new SqlColumnToEntityFieldMapping(3, countryIdMapping, "name"),
                        // Column 4 (ct.id) mapping to foreign entity Country "continent" foreign field
                        continentIdMapping = new SqlColumnToEntityFieldMapping(4, countryIdMapping, "continent", "Continent"),
                        // Column 5 (ct.name): simple mapping to foreign entity Continent "name" field
                        new SqlColumnToEntityFieldMapping(5, continentIdMapping, "name")
                )
        );
        future.setHandler(asyncResult ->
                Platform.log(SqlResultToEntityListGenerator.createEntityList(asyncResult.result(), mapping, new EntityStore(), "test"))
        );
    }

    /*
    public static void testBase64() {
        try {

            // Encode using basic encoder
            String base64encodedString = Base64.getEncoder().encodeToString("TutorialsPoint?java8".getBytes("utf-8"));
            Platform.log("Base64 Encoded String (Basic) :" + base64encodedString);

            // Decode
            byte[] base64decodedBytes = Base64.getDecoder().decode(base64encodedString);

            Platform.log("Original String: " + new String(base64decodedBytes, "utf-8"));
            base64encodedString = Base64.getUrlEncoder().encodeToString("TutorialsPoint?java8".getBytes("utf-8"));
            Platform.log("Base64 Encoded String (URL) :" + base64encodedString);
        }catch(UnsupportedEncodingException e){
            Platform.log("Error :" + e.getMessage());
        }
    }

    public static void testJavaFastPFOR() {
        superSimpleExample();
        unsortedExample();
        basicExample();
        advancedExample();
        headlessDemo();
    }

    public static void superSimpleExample() {
        IntegratedIntCompressor iic = new IntegratedIntCompressor();
        int[] data = new int[2342351];
        for(int k = 0; k < data.length; ++k)
            data[k] = k;
        Platform.log("Compressing "+data.length+" integers using friendly interface");
        int[] compressed = iic.compress(data);
        int[] recov = iic.uncompress(compressed);
        Platform.log("compressed from "+data.length*4/1024+"KB to "+compressed.length*4/1024+"KB");
        if(!Arrays.equals(recov,data)) throw new RuntimeException("bug");
    }

    public static void basicExample() {
        int[] data = new int[2342351];
        Platform.log("Compressing "+data.length+" integers in one go");
        // data should be sorted for best
        //results
        for(int k = 0; k < data.length; ++k)
            data[k] = k;
        // Very important: the data is in sorted order!!! If not, you
        // will get very poor compression with IntegratedBinaryPacking,
        // you should use another CODEC.

        // next we compose a CODEC. Most of the processing
        // will be done with binary packing, and leftovers will
        // be processed using variable byte
        IntegratedIntegerCODEC codec =  new
                IntegratedComposition(
                new IntegratedBinaryPacking(),
                new IntegratedVariableByte());
        // output vector should be large enough...
        int [] compressed = new int[data.length+1024];
        // compressed might not be large enough in some cases
        // if you get java.lang.ArrayIndexOutOfBoundsException, try
        // allocating more memory

        // compressing
        IntWrapper inputoffset = new IntWrapper(0);
        IntWrapper outputoffset = new IntWrapper(0);
        codec.compress(data,inputoffset,data.length,compressed,outputoffset);
        // got it!
        // inputoffset should be at data.length but outputoffset tells
        // us where we are...
        Platform.log("compressed from "+data.length*4/1024+"KB to "+outputoffset.intValue()*4/1024+"KB");
        // we can repack the data: (optional)
        compressed = Arrays.copyOf(compressed,outputoffset.intValue());

        // now uncompressing
        int[] recovered = new int[data.length];
        IntWrapper recoffset = new IntWrapper(0);
        codec.uncompress(compressed,new IntWrapper(0),compressed.length,recovered,recoffset);
        if(Arrays.equals(data,recovered))
            Platform.log("data is recovered without loss");
        else
            throw new RuntimeException("bug"); // could use assert
        Platform.log("");
    }


 // This is an example to show you can compress unsorted integers
 // as long as most are small.

    public static void unsortedExample() {
        final int N = 1333333;
        int[] data = new int[N];
        // initialize the data (most will be small
        for(int k = 0; k < N; k+=1) data[k] = 3;
        // throw some larger values
        for(int k = 0; k < N; k+=5) data[k] = 100;
        for(int k = 0; k < N; k+=533) data[k] = 10000;
        int[] compressed = new int [N+1024];// could need more
        IntegerCODEC codec =  new
                Composition(
                new FastPFOR(),
                new VariableByte());
        // compressing
        IntWrapper inputoffset = new IntWrapper(0);
        IntWrapper outputoffset = new IntWrapper(0);
        codec.compress(data,inputoffset,data.length,compressed,outputoffset);
        Platform.log("compressed unsorted integers from "+data.length*4/1024+"KB to "+outputoffset.intValue()*4/1024+"KB");
        // we can repack the data: (optional)
        compressed = Arrays.copyOf(compressed,outputoffset.intValue());

        int[] recovered = new int[N];
        IntWrapper recoffset = new IntWrapper(0);
        codec.uncompress(compressed,new IntWrapper(0),compressed.length,recovered,recoffset);
        if(Arrays.equals(data,recovered))
            Platform.log("data is recovered without loss");
        else
            throw new RuntimeException("bug"); // could use assert
        Platform.log("");

    }

 // This is like the basic example, but we
 // show how to process larger arrays in chunks.
 //
 // Some of this code was written by Pavel Klinov.

    public static void advancedExample() {
        int TotalSize = 2342351; // some arbitrary number
        int ChunkSize = 16384; // size of each chunk, choose a multiple of 128
        Platform.log("Compressing "+TotalSize+" integers using chunks of "+ChunkSize+" integers ("+ChunkSize*4/1024+"KB)");
        Platform.log("(It is often better for applications to work in chunks fitting in CPU cache.)");
        int[] data = new int[TotalSize];
        // data should be sorted for best
        //results
        for(int k = 0; k < data.length; ++k)
            data[k] = k;
        // next we compose a CODEC. Most of the processing
        // will be done with binary packing, and leftovers will
        // be processed using variable byte, using variable byte
        // only for the last chunk!
        IntegratedIntegerCODEC regularcodec =  new
                IntegratedBinaryPacking();
        IntegratedVariableByte ivb = new IntegratedVariableByte();
        IntegratedIntegerCODEC lastcodec =  new
                IntegratedComposition(regularcodec,ivb);
        // output vector should be large enough...
        int [] compressed = new int[TotalSize+1024];


         // compressing
        IntWrapper inputoffset = new IntWrapper(0);
        IntWrapper outputoffset = new IntWrapper(0);
        for(int k = 0; k < TotalSize / ChunkSize; ++k)
            regularcodec.compress(data,inputoffset,ChunkSize,compressed,outputoffset);
        lastcodec.compress(data, inputoffset, TotalSize % ChunkSize, compressed, outputoffset);
        // got it!
        // inputoffset should be at data.length but outputoffset tells
        // us where we are...
        Platform.log("compressed from "+data.length*4/1024+"KB to "+outputoffset.intValue()*4/1024+"KB");
        // we can repack the data:
        compressed = Arrays.copyOf(compressed,outputoffset.intValue());


         // now uncompressing
         // We are *not* assuming that the original array length is known, however
         // we assume that the chunk size (ChunkSize) is known.
        int[] recovered = new int[ChunkSize];
        IntWrapper compoff = new IntWrapper(0);
        IntWrapper recoffset;
        int currentpos = 0;

        while(compoff.get()<compressed.length) {
            recoffset = new IntWrapper(0);
            regularcodec.uncompress(compressed,compoff,compressed.length - compoff.get(),recovered,recoffset);

            if(recoffset.get() < ChunkSize) {// last chunk detected
                ivb.uncompress(compressed,compoff,compressed.length - compoff.get(),recovered,recoffset);
            }
            for(int i = 0; i < recoffset.get(); ++i) {
                if(data[currentpos+i] != recovered[i]) throw new RuntimeException("bug"); // could use assert
            }
            currentpos += recoffset.get();
        }
        Platform.log("data is recovered without loss");
        Platform.log("");

    }


    //  Demo of the headless approach where we must supply the array length

    public static void headlessDemo() {
        Platform.log("Compressing arrays with minimal header...");
        int[] uncompressed1 = {1,2,1,3,1};
        int[] uncompressed2 = {3,2,4,6,1};

        int[] compressed = new int[uncompressed1.length+uncompressed2.length+1024];

        SkippableIntegerCODEC codec = new SkippableComposition(new BinaryPacking(), new VariableByte());

        // compressing
        IntWrapper outPos = new IntWrapper();

        IntWrapper previous = new IntWrapper();

        codec.headlessCompress(uncompressed1,new IntWrapper(),uncompressed1.length,compressed,outPos);
        int length1 = outPos.get() - previous.get();
        previous = new IntWrapper(outPos.get());
        codec.headlessCompress(uncompressed2,new IntWrapper(),uncompressed2.length,compressed,outPos);
        int length2 = outPos.get() - previous.get();



        compressed = Arrays.copyOf(compressed,length1 + length2);
        Platform.log("compressed unsorted integers from "+uncompressed1.length*4+"B to "+length1*4+"B");
        Platform.log("compressed unsorted integers from "+uncompressed2.length*4+"B to "+length2*4+"B");
        Platform.log("Total compressed output "+compressed.length);

        int[] recovered1 = new int[uncompressed1.length];
        int[] recovered2 = new int[uncompressed1.length];
        IntWrapper inPos = new IntWrapper();
        Platform.log("Decoding first array starting at pos = "+inPos);
        codec.headlessUncompress(compressed,inPos, compressed.length, recovered1, new IntWrapper(0), uncompressed1.length);
        Platform.log("Decoding second array starting at pos = "+inPos);
        codec.headlessUncompress(compressed,inPos, compressed.length, recovered2, new IntWrapper(0), uncompressed2.length);
        if(!Arrays.equals(uncompressed1,recovered1)) throw new RuntimeException("First array does not match.");
        if(!Arrays.equals(uncompressed2,recovered2)) throw new RuntimeException("Second array does not match.");
        Platform.log("The arrays match, your code is probably ok.");
    }
    */
}
