/**
 * @author Bruno Salmon
 */
module webfx.framework {

    requires webfx.scheduler;
    requires webfx.util;
    requires webfx.type;
    requires webfx.noreflect;
    requires webfx.platform;
    requires webfx.fxkits.core;
    requires webfx.lib.javacupruntime;
    requires webfx.lib.rxjava;
    requires webfx.lib.controlsfx.validation;

    requires static javafx.controls;

    exports webfx.framework.activity.base.elementals.activeproperty;
    exports webfx.framework.activity.base.combinations.domainapplication;
    exports webfx.framework.activity.base.combinations.domainapplication.impl;
    exports webfx.framework.activity.base.combinations.domainpresentation;
    exports webfx.framework.activity.base.combinations.domainpresentation.impl;
    exports webfx.framework.activity.base.combinations.domainpresentationlogic.impl;
    exports webfx.framework.activity.base.combinations.viewapplication;
    exports webfx.framework.activity.base.combinations.viewapplication.impl;
    exports webfx.framework.activity.base.combinations.viewdomain;
    exports webfx.framework.activity.base.combinations.viewdomain.impl;
    exports webfx.framework.activity.base.combinations.viewdomainapplication;
    exports webfx.framework.activity.base.combinations.viewdomainapplication.impl;
    exports webfx.framework.activity.base.elementals.domain;
    exports webfx.framework.activity.base.elementals.domain.impl;
    exports webfx.framework.activity.i18n;
    exports webfx.framework.activity.i18n.impl;
    exports webfx.framework.activity.base.elementals.presentation;
    exports webfx.framework.activity.base.elementals.presentation.impl;
    exports webfx.framework.activity.base.elementals.presentation.logic;
    exports webfx.framework.activity.base.elementals.presentation.logic.impl;
    exports webfx.framework.activity.base.elementals.presentation.view;
    exports webfx.framework.activity.base.elementals.presentation.view.impl;
    exports webfx.framework.activity.base.elementals.uiroute;
    exports webfx.framework.activity.base.elementals.uiroute.impl;
    exports webfx.framework.activity.base.elementals.view;
    exports webfx.framework.activity.base.elementals.view.impl;
    exports webfx.framework.expression;
    exports webfx.framework.expression.lci;
    exports webfx.framework.expression.parser;
    exports webfx.framework.expression.sqlcompiler;
    exports webfx.framework.expression.sqlcompiler.sql;
    exports webfx.framework.expression.terms;
    exports webfx.framework.expression.terms.function;
    exports webfx.framework.orm.domainmodel;
    exports webfx.framework.orm.domainmodel.loader;
    exports webfx.framework.orm.entity;
    exports webfx.framework.orm.entity.impl;
    exports webfx.framework.orm.mapping;
    exports webfx.framework.router;
    exports webfx.framework.router.handler;
    exports webfx.framework.session;
    exports webfx.framework.ui.action;
    exports webfx.framework.ui.auth;
    exports webfx.framework.ui.graphic.border;
    exports webfx.framework.ui.filter;
    exports webfx.framework.ui.formatter;
    exports webfx.framework.services.i18n;
    exports webfx.framework.ui.mapping;
    exports webfx.framework.ui.uirouter;
    exports webfx.framework.ui.filter.rx;
}