/**
 * @author Bruno Salmon
 */
module naga.framework {

    requires naga.scheduler;
    requires naga.util;
    requires naga.type;
    requires naga.noreflect;
    requires naga.platform;
    requires naga.fx;
    requires naga.javalib.javacupruntime;
    requires naga.javalib.rxjava;
    requires naga.javalib.controlsfx.validation;

    requires static javafx.controls;

    exports naga.framework.activity.activeproperty;
    exports naga.framework.activity.combinations.domainapplication;
    exports naga.framework.activity.combinations.domainapplication.impl;
    exports naga.framework.activity.combinations.domainpresentation;
    exports naga.framework.activity.combinations.domainpresentation.impl;
    exports naga.framework.activity.combinations.domainpresentationlogic.impl;
    exports naga.framework.activity.combinations.viewapplication;
    exports naga.framework.activity.combinations.viewapplication.impl;
    exports naga.framework.activity.combinations.viewdomain;
    exports naga.framework.activity.combinations.viewdomain.impl;
    exports naga.framework.activity.combinations.viewdomainapplication;
    exports naga.framework.activity.combinations.viewdomainapplication.impl;
    exports naga.framework.activity.domain;
    exports naga.framework.activity.domain.impl;
    exports naga.framework.activity.i18n;
    exports naga.framework.activity.i18n.impl;
    exports naga.framework.activity.presentation;
    exports naga.framework.activity.presentation.impl;
    exports naga.framework.activity.presentation.logic;
    exports naga.framework.activity.presentation.logic.impl;
    exports naga.framework.activity.presentation.view;
    exports naga.framework.activity.presentation.view.impl;
    exports naga.framework.activity.uiroute;
    exports naga.framework.activity.uiroute.impl;
    exports naga.framework.activity.view;
    exports naga.framework.activity.view.impl;
    exports naga.framework.expression;
    exports naga.framework.expression.lci;
    exports naga.framework.expression.parser;
    exports naga.framework.expression.sqlcompiler;
    exports naga.framework.expression.sqlcompiler.sql;
    exports naga.framework.expression.terms;
    exports naga.framework.expression.terms.function;
    exports naga.framework.orm.domainmodel;
    exports naga.framework.orm.domainmodel.loader;
    exports naga.framework.orm.entity;
    exports naga.framework.orm.entity.impl;
    exports naga.framework.orm.mapping;
    exports naga.framework.router;
    exports naga.framework.router.handler;
    exports naga.framework.session;
    exports naga.framework.ui.action;
    exports naga.framework.ui.auth;
    exports naga.framework.ui.graphic.border;
    exports naga.framework.ui.filter;
    exports naga.framework.ui.formatter;
    exports naga.framework.services.i18n;
    exports naga.framework.ui.mapping;
    exports naga.framework.ui.uirouter;
    exports naga.framework.ui.rx;
}