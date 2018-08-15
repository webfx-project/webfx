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

    exports naga.framework.activity.base.elementals.activeproperty;
    exports naga.framework.activity.base.combinations.domainapplication;
    exports naga.framework.activity.base.combinations.domainapplication.impl;
    exports naga.framework.activity.base.combinations.domainpresentation;
    exports naga.framework.activity.base.combinations.domainpresentation.impl;
    exports naga.framework.activity.base.combinations.domainpresentationlogic.impl;
    exports naga.framework.activity.base.combinations.viewapplication;
    exports naga.framework.activity.base.combinations.viewapplication.impl;
    exports naga.framework.activity.base.combinations.viewdomain;
    exports naga.framework.activity.base.combinations.viewdomain.impl;
    exports naga.framework.activity.base.combinations.viewdomainapplication;
    exports naga.framework.activity.base.combinations.viewdomainapplication.impl;
    exports naga.framework.activity.base.elementals.domain;
    exports naga.framework.activity.base.elementals.domain.impl;
    exports naga.framework.activity.i18n;
    exports naga.framework.activity.i18n.impl;
    exports naga.framework.activity.base.elementals.presentation;
    exports naga.framework.activity.base.elementals.presentation.impl;
    exports naga.framework.activity.base.elementals.presentation.logic;
    exports naga.framework.activity.base.elementals.presentation.logic.impl;
    exports naga.framework.activity.base.elementals.presentation.view;
    exports naga.framework.activity.base.elementals.presentation.view.impl;
    exports naga.framework.activity.base.elementals.uiroute;
    exports naga.framework.activity.base.elementals.uiroute.impl;
    exports naga.framework.activity.base.elementals.view;
    exports naga.framework.activity.base.elementals.view.impl;
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
    exports naga.framework.ui.filter.rx;
}