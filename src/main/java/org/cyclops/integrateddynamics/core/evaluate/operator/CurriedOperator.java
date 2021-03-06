package org.cyclops.integrateddynamics.core.evaluate.operator;

import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.integrateddynamics.api.evaluate.EvaluationException;
import org.cyclops.integrateddynamics.api.evaluate.operator.IOperator;
import org.cyclops.integrateddynamics.api.evaluate.operator.IOperatorSerializer;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValue;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValueType;
import org.cyclops.integrateddynamics.api.evaluate.variable.IVariable;
import org.cyclops.integrateddynamics.api.logicprogrammer.IConfigRenderPattern;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypes;
import org.cyclops.integrateddynamics.core.evaluate.variable.Variable;
import org.cyclops.integrateddynamics.core.helper.L10NValues;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * An operator that is partially being applied.
 * @author rubensworks
 */
public class CurriedOperator implements IOperator {

    private final IOperator baseOperator;
    private final IVariable appliedVariable;

    public CurriedOperator(IOperator baseOperator, IVariable appliedVariable) {
        this.baseOperator = baseOperator;
        this.appliedVariable = appliedVariable;
    }

    protected String getAppliedSymbol() {
        return appliedVariable.getType().getTypeName();
    }

    @Override
    public String getSymbol() {
        StringBuilder sb = new StringBuilder();
        sb.append(baseOperator.getSymbol());
        sb.append(" [");
        sb.append(getAppliedSymbol());
        sb.append("]");
        return sb.toString();
    }

    @Override
    public String getUniqueName() {
        return "curriedOperator";
    }

    @Override
    public String getUnlocalizedName() {
        return baseOperator.getUnlocalizedName();
    }

    @Override
    public String getUnlocalizedCategoryName() {
        return baseOperator.getUnlocalizedCategoryName();
    }

    @Override
    public String getLocalizedNameFull() {
        return L10NHelpers.localize(L10NValues.OPERATOR_APPLIED_OPERATORNAME,
                baseOperator.getLocalizedNameFull(), getAppliedSymbol());
    }

    @Override
    public void loadTooltip(List<String> lines, boolean appendOptionalInfo) {
        baseOperator.loadTooltip(lines, appendOptionalInfo);
        lines.add(L10NHelpers.localize(L10NValues.OPERATOR_APPLIED_TYPE, getAppliedSymbol()));
    }

    @Override
    public IValueType[] getInputTypes() {
        IValueType[] baseInputTypes = baseOperator.getInputTypes();
        return Arrays.copyOfRange(baseInputTypes, 1, baseInputTypes.length);
    }

    @Override
    public IValueType getOutputType() {
        return baseOperator.getOutputType();
    }

    protected IVariable[] deriveFullInputVariables(IVariable[] partialInput) {
        IVariable[] fullInput = new IVariable[Math.min(baseOperator.getRequiredInputLength(), partialInput.length + 1)];
        fullInput[0] = appliedVariable;
        System.arraycopy(partialInput, 0, fullInput, 1, fullInput.length - 1);
        return fullInput;
    }

    protected IValueType[] deriveFullInputTypes(IValueType[] partialInput) {
        IValueType[] fullInput = new IValueType[Math.min(baseOperator.getRequiredInputLength(), partialInput.length + 1)];
        fullInput[0] = appliedVariable.getType();
        System.arraycopy(partialInput, 0, fullInput, 1, fullInput.length - 1);
        return fullInput;
    }

    @Override
    public IValueType getConditionalOutputType(IVariable[] input) {
        return baseOperator.getConditionalOutputType(deriveFullInputVariables(input));
    }

    @Override
    public IValue evaluate(IVariable[] input) throws EvaluationException {
        return baseOperator.evaluate(deriveFullInputVariables(input));
    }

    @Override
    public int getRequiredInputLength() {
        return baseOperator.getRequiredInputLength() - 1;
    }

    @Override
    public L10NHelpers.UnlocalizedString validateTypes(IValueType[] input) {
        return baseOperator.validateTypes(deriveFullInputTypes(input));
    }

    @Override
    public IConfigRenderPattern getRenderPattern() {
        return IConfigRenderPattern.NONE;
    }

    @Override
    public IOperator materialize() throws EvaluationException {
        return new CurriedOperator(baseOperator, new Variable(appliedVariable.getType(), appliedVariable.getValue()));
    }

    public static class Serializer implements IOperatorSerializer<CurriedOperator> {

        @Override
        public boolean canHandle(IOperator operator) {
            return operator instanceof CurriedOperator;
        }

        @Override
        public String getUniqueName() {
            return "curry";
        }

        @Override
        public String serialize(CurriedOperator operator) {
            IValue value;
            try {
                value = operator.appliedVariable.getValue();
            } catch (EvaluationException e) {
                value = operator.appliedVariable.getType().getDefault();
            }
            IValueType valueType = value.getType();
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("valueType", valueType.getUnlocalizedName());
            tag.setString("value", valueType.serialize(value));
            tag.setString("baseOperator", Operators.REGISTRY.serialize(operator.baseOperator));
            return tag.toString();
        }

        @Override
        public CurriedOperator deserialize(String valueOperator) throws EvaluationException {
            NBTTagCompound tag;
            try {
                tag = JsonToNBT.getTagFromJson(valueOperator);
            } catch (NBTException e) {
                e.printStackTrace();
                throw new EvaluationException(e.getMessage());
            }
            IValueType valueType = ValueTypes.REGISTRY.getValueType(tag.getString("valueType"));
            IValue value = valueType.deserialize(tag.getString("value"));
            IOperator baseOperator = Objects.requireNonNull(Operators.REGISTRY.deserialize(tag.getString("baseOperator")));
            return new CurriedOperator(baseOperator, new Variable(valueType, value));
        }
    }
}
