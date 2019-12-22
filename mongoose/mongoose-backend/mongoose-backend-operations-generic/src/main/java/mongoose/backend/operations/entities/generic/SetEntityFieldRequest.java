package mongoose.backend.operations.entities.generic;

import javafx.scene.layout.Pane;
import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.operation.HasOperationCode;
import webfx.framework.shared.operation.HasOperationExecutor;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.HasEntity;
import webfx.platform.shared.util.async.AsyncFunction;

public abstract class SetEntityFieldRequest implements HasOperationCode,
        HasEntity,
        HasOperationExecutor<SetEntityFieldRequest, Void> {

    private final Entity entity;
    private final Expression<Entity> leftExpression;
    private final Expression<Entity> rightExpression;
    private final String confirmationText;
    private final Pane parentContainer;

    public SetEntityFieldRequest(Entity entity, String leftExpression, String rightExpression, String confirmationText, Pane parentContainer) {
        // The request may be instantiated with entity = null, so must be null friendly
        this(entity, entity == null ? null : entity.parseExpression(leftExpression), entity == null ? null : entity.parseExpression(rightExpression), confirmationText, parentContainer);
    }

    public SetEntityFieldRequest(Entity entity, Expression<Entity> leftExpression, Expression<Entity> rightExpression, String confirmationText, Pane parentContainer) {
        this.entity = entity;
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
        this.confirmationText = confirmationText;
        this.parentContainer = parentContainer;
    }

    @Override
    public Entity getEntity() {
        return entity;
    }

    Expression<Entity> getLeftExpression() {
        return leftExpression;
    }

    Expression<Entity> getRightExpression() {
        return rightExpression;
    }

    String getConfirmationText() {
        return confirmationText;
    }

    Pane getParentContainer() {
        return parentContainer;
    }

    @Override
    public AsyncFunction<SetEntityFieldRequest, Void> getOperationExecutor() {
        return SetEntityFieldExecutor::executeRequest;
    }
}
