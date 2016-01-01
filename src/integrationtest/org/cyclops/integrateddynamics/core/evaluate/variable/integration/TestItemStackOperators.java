package org.cyclops.integrateddynamics.core.evaluate.variable.integration;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.apache.http.util.Asserts;
import org.cyclops.cyclopscore.helper.EnchantmentHelpers;
import org.cyclops.integrateddynamics.api.evaluate.EvaluationException;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValue;
import org.cyclops.integrateddynamics.api.evaluate.variable.IVariable;
import org.cyclops.integrateddynamics.core.evaluate.operator.Operators;
import org.cyclops.integrateddynamics.core.evaluate.variable.*;
import org.cyclops.integrateddynamics.core.test.IntegrationBefore;
import org.cyclops.integrateddynamics.core.test.IntegrationTest;
import org.cyclops.integrateddynamics.core.test.TestHelpers;

/**
 * Test the different logical operators.
 * @author rubensworks
 */
public class TestItemStackOperators {

    private static final DummyValueType DUMMY_TYPE = DummyValueType.TYPE;
    private static final DummyVariable<DummyValueType.DummyValue> DUMMY_VARIABLE =
            new DummyVariable<DummyValueType.DummyValue>(DUMMY_TYPE, DummyValueType.DummyValue.of());

    private DummyVariableItemStack iApple;
    private DummyVariableItemStack iApple2;
    private DummyVariableItemStack iBeef;
    private DummyVariableItemStack iEnderPearl;
    private DummyVariableItemStack iHoe;
    private DummyVariableItemStack iHoe100;
    private DummyVariableItemStack iHoeEnchanted;
    private DummyVariableItemStack iPickaxe;
    private DummyVariableItemStack iStone;
    private DummyVariableItemStack iBucketLava;

    private DummyVariableBlock bStone;
    private DummyVariableBlock bObsidian;

    @IntegrationBefore
    public void before() {
        iApple = new DummyVariableItemStack(ValueObjectTypeItemStack.ValueItemStack.of(new ItemStack(Items.apple)));
        iApple2 = new DummyVariableItemStack(ValueObjectTypeItemStack.ValueItemStack.of(new ItemStack(Items.apple, 2)));
        iBeef = new DummyVariableItemStack(ValueObjectTypeItemStack.ValueItemStack.of(new ItemStack(Items.beef)));
        iEnderPearl = new DummyVariableItemStack(ValueObjectTypeItemStack.ValueItemStack.of(new ItemStack(Items.ender_pearl)));
        iHoe = new DummyVariableItemStack(ValueObjectTypeItemStack.ValueItemStack.of(new ItemStack(Items.diamond_hoe)));
        iHoe100 = new DummyVariableItemStack(ValueObjectTypeItemStack.ValueItemStack.of(new ItemStack(Items.diamond_hoe, 1, 100)));
        ItemStack hoeEnchanted = new ItemStack(Items.diamond_hoe);
        EnchantmentHelpers.setEnchantmentLevel(hoeEnchanted, Enchantment.aquaAffinity, 1);
        hoeEnchanted.setRepairCost(10);
        iHoeEnchanted = new DummyVariableItemStack(ValueObjectTypeItemStack.ValueItemStack.of(hoeEnchanted));
        iPickaxe = new DummyVariableItemStack(ValueObjectTypeItemStack.ValueItemStack.of(new ItemStack(Items.diamond_pickaxe)));
        iStone = new DummyVariableItemStack(ValueObjectTypeItemStack.ValueItemStack.of(new ItemStack(Blocks.stone)));
        iBucketLava = new DummyVariableItemStack(ValueObjectTypeItemStack.ValueItemStack.of(new ItemStack(Items.lava_bucket)));

        bStone = new DummyVariableBlock(ValueObjectTypeBlock.ValueBlock.of(Blocks.stone.getDefaultState()));
        bObsidian = new DummyVariableBlock(ValueObjectTypeBlock.ValueBlock.of(Blocks.obsidian.getDefaultState()));
    }

    /**
     * ----------------------------------- SIZE -----------------------------------
     */

    @IntegrationTest
    public void testItemStackSize() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_SIZE.evaluate(new IVariable[]{iApple});
        Asserts.check(res1 instanceof ValueTypeInteger.ValueInteger, "result is an integer");
        TestHelpers.assertEqual(((ValueTypeInteger.ValueInteger) res1).getRawValue(), 1, "size(apple:1) = 1");

        IValue res2 = Operators.OBJECT_ITEMSTACK_SIZE.evaluate(new IVariable[]{iApple2});
        TestHelpers.assertEqual(((ValueTypeInteger.ValueInteger) res2).getRawValue(), 2, "size(apple:2) = 2");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputSizeSizeLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_SIZE.evaluate(new IVariable[]{iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputSizeSizeSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_SIZE.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeSize() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_SIZE.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- MAXSIZE -----------------------------------
     */

    @IntegrationTest
    public void testItemStackMaxSize() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_MAXSIZE.evaluate(new IVariable[]{iApple});
        Asserts.check(res1 instanceof ValueTypeInteger.ValueInteger, "result is an integer");
        TestHelpers.assertEqual(((ValueTypeInteger.ValueInteger) res1).getRawValue(), 64, "maxsize(apple) = 64");

        IValue res2 = Operators.OBJECT_ITEMSTACK_MAXSIZE.evaluate(new IVariable[]{iEnderPearl});
        TestHelpers.assertEqual(((ValueTypeInteger.ValueInteger) res2).getRawValue(), 16, "maxsize(enderpearl) = 16");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputMaxSizeMaxSizeLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_MAXSIZE.evaluate(new IVariable[]{iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputMaxSizeMaxSizeSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_MAXSIZE.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeMaxSize() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_MAXSIZE.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- ISSTACKABLE -----------------------------------
     */

    @IntegrationTest
    public void testItemStackIsStackable() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_ISSTACKABLE.evaluate(new IVariable[]{iApple});
        Asserts.check(res1 instanceof ValueTypeBoolean.ValueBoolean, "result is a boolean");
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), true, "isstackable(apple) = true");

        IValue res2 = Operators.OBJECT_ITEMSTACK_ISSTACKABLE.evaluate(new IVariable[]{iHoe});
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res2).getRawValue(), false, "isstackable(hoe) = false");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputIsStackableIsStackableLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISSTACKABLE.evaluate(new IVariable[]{iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputIsStackableIsStackableSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISSTACKABLE.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeIsStackable() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISSTACKABLE.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- ISDAMAGEABLE -----------------------------------
     */

    @IntegrationTest
    public void testItemStackIsDamageable() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_ISDAMAGEABLE.evaluate(new IVariable[]{iApple});
        Asserts.check(res1 instanceof ValueTypeBoolean.ValueBoolean, "result is a boolean");
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), false, "isdamageable(apple) = false");

        IValue res2 = Operators.OBJECT_ITEMSTACK_ISDAMAGEABLE.evaluate(new IVariable[]{iHoe});
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res2).getRawValue(), true, "isdamageable(hoe) = true");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputIsDamageableIsDamageableLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISDAMAGEABLE.evaluate(new IVariable[]{iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputIsDamageableIsDamageableSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISDAMAGEABLE.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeIsDamageable() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISDAMAGEABLE.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- DAMAGE -----------------------------------
     */

    @IntegrationTest
    public void testItemStackDamage() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_DAMAGE.evaluate(new IVariable[]{iHoe});
        Asserts.check(res1 instanceof ValueTypeInteger.ValueInteger, "result is an integer");
        TestHelpers.assertEqual(((ValueTypeInteger.ValueInteger) res1).getRawValue(), 0, "damage(hoe:0) = 0");

        IValue res2 = Operators.OBJECT_ITEMSTACK_DAMAGE.evaluate(new IVariable[]{iHoe100});
        TestHelpers.assertEqual(((ValueTypeInteger.ValueInteger) res2).getRawValue(), 100, "damage(hoe:100) = 100");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputDamageDamageLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_DAMAGE.evaluate(new IVariable[]{iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputDamageDamageSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_DAMAGE.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeDamage() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_DAMAGE.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- MAXDAMAGE -----------------------------------
     */

    @IntegrationTest
    public void testItemStackMaxDamage() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_MAXDAMAGE.evaluate(new IVariable[]{iApple});
        Asserts.check(res1 instanceof ValueTypeInteger.ValueInteger, "result is an integer");
        TestHelpers.assertEqual(((ValueTypeInteger.ValueInteger) res1).getRawValue(), 0, "maxdamage(apple) = 0");

        IValue res2 = Operators.OBJECT_ITEMSTACK_MAXDAMAGE.evaluate(new IVariable[]{iHoe});
        TestHelpers.assertEqual(((ValueTypeInteger.ValueInteger) res2).getRawValue(), 1561, "maxdamage(hoe) = 1561");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputMaxDamageMaxDamageLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_MAXDAMAGE.evaluate(new IVariable[]{iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputMaxDamageMaxDamageSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_MAXDAMAGE.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeMaxDamage() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_MAXDAMAGE.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- ISENCHANTED -----------------------------------
     */

    @IntegrationTest
    public void testItemStackIsEnchanted() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_ISENCHANTED.evaluate(new IVariable[]{iHoe});
        Asserts.check(res1 instanceof ValueTypeBoolean.ValueBoolean, "result is a boolean");
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), false, "isenchanted(hoe) = false");

        IValue res2 = Operators.OBJECT_ITEMSTACK_ISENCHANTED.evaluate(new IVariable[]{iHoeEnchanted});
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res2).getRawValue(), true, "isenchanted(hoeenchanted) = true");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputIsEnchantedIsEnchantedLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISENCHANTED.evaluate(new IVariable[]{iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputIsEnchantedIsEnchantedSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISENCHANTED.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeIsEnchanted() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISENCHANTED.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- ISENCHANTABLE -----------------------------------
     */

    @IntegrationTest
    public void testItemStackIsEnchantable() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_ISENCHANTABLE.evaluate(new IVariable[]{iApple});
        Asserts.check(res1 instanceof ValueTypeBoolean.ValueBoolean, "result is a boolean");
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), false, "isenchantable(apple) = false");

        IValue res2 = Operators.OBJECT_ITEMSTACK_ISENCHANTABLE.evaluate(new IVariable[]{iHoe});
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res2).getRawValue(), true, "isenchantable(hoe) = true");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputIsEnchantableIsEnchantableLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISENCHANTABLE.evaluate(new IVariable[]{iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputIsEnchantableIsEnchantableSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISENCHANTABLE.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeIsEnchantable() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISENCHANTABLE.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- REPAIRCOST -----------------------------------
     */

    @IntegrationTest
    public void testItemStackRepairCost() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_REPAIRCOST.evaluate(new IVariable[]{iApple});
        Asserts.check(res1 instanceof ValueTypeInteger.ValueInteger, "result is an integer");
        TestHelpers.assertEqual(((ValueTypeInteger.ValueInteger) res1).getRawValue(), 0, "repaircost(apple) = 0");

        IValue res2 = Operators.OBJECT_ITEMSTACK_REPAIRCOST.evaluate(new IVariable[]{iHoeEnchanted});
        TestHelpers.assertEqual(((ValueTypeInteger.ValueInteger) res2).getRawValue(), 10, "repaircost(hoe:10) = 10");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputRepairCostRepairCostLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_REPAIRCOST.evaluate(new IVariable[]{iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputRepairCostRepairCostSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_REPAIRCOST.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeRepairCost() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_REPAIRCOST.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- RARITY -----------------------------------
     */

    @IntegrationTest
    public void testItemStackRarity() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_RARITY.evaluate(new IVariable[]{iApple});
        Asserts.check(res1 instanceof ValueTypeString.ValueString, "result is an integer");
        TestHelpers.assertEqual(((ValueTypeString.ValueString) res1).getRawValue(), EnumRarity.COMMON.rarityName, "rarity(apple) = common");

        IValue res2 = Operators.OBJECT_ITEMSTACK_RARITY.evaluate(new IVariable[]{iHoeEnchanted});
        TestHelpers.assertEqual(((ValueTypeString.ValueString) res2).getRawValue(), EnumRarity.RARE.rarityName, "rarity(hoeenchanted) = rare");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputRarityRarityLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_RARITY.evaluate(new IVariable[]{iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputRarityRaritySmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_RARITY.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeRarity() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_RARITY.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- STRENGTH_VS_BLOCK -----------------------------------
     */

    @IntegrationTest
    public void testItemStackStrengthVsBlock() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_STRENGTH_VS_BLOCK.evaluate(new IVariable[]{iHoe, bStone});
        Asserts.check(res1 instanceof ValueTypeDouble.ValueDouble, "result is a double");
        TestHelpers.assertEqual(((ValueTypeDouble.ValueDouble) res1).getRawValue(), 1.0D, "strengthvsblock(hoe, stone) = 1.0");

        IValue res2 = Operators.OBJECT_ITEMSTACK_STRENGTH_VS_BLOCK.evaluate(new IVariable[]{iPickaxe, bStone});
        TestHelpers.assertEqual(((ValueTypeDouble.ValueDouble) res2).getRawValue(), 8.0D, "strengthvsblock(pickaxe, stone) = 8.0");

        IValue res3 = Operators.OBJECT_ITEMSTACK_STRENGTH_VS_BLOCK.evaluate(new IVariable[]{iPickaxe, bObsidian});
        TestHelpers.assertEqual(((ValueTypeDouble.ValueDouble) res3).getRawValue(), 8.0D, "strengthvsblock(pickaxe, obsidian) = 8.0");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputStrengthVsBlockStrengthVsBlockLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_STRENGTH_VS_BLOCK.evaluate(new IVariable[]{iApple, iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputStrengthVsBlockStrengthVsBlockSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_STRENGTH_VS_BLOCK.evaluate(new IVariable[]{iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeStrengthVsBlock() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_STRENGTH_VS_BLOCK.evaluate(new IVariable[]{DUMMY_VARIABLE, DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- CAN_HARVEST_BLOCK -----------------------------------
     */

    @IntegrationTest
    public void testItemStackCanHarvestBlock() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_CAN_HARVEST_BLOCK.evaluate(new IVariable[]{iHoe, bStone});
        Asserts.check(res1 instanceof ValueTypeBoolean.ValueBoolean, "result is a boolean");
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), false, "canharvestblock(hoe, stone) = false");

        IValue res2 = Operators.OBJECT_ITEMSTACK_CAN_HARVEST_BLOCK.evaluate(new IVariable[]{iPickaxe, bStone});
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res2).getRawValue(), true, "canharvestblock(pickaxe, stone) = true");

        IValue res3 = Operators.OBJECT_ITEMSTACK_CAN_HARVEST_BLOCK.evaluate(new IVariable[]{iPickaxe, bObsidian});
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res3).getRawValue(), true, "canharvestblock(pickaxe, obsidian) = true");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputCanHarvestBlockCanHarvestBlockLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_CAN_HARVEST_BLOCK.evaluate(new IVariable[]{iApple, iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputCanHarvestBlockCanHarvestBlockSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_CAN_HARVEST_BLOCK.evaluate(new IVariable[]{iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeCanHarvestBlock() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_CAN_HARVEST_BLOCK.evaluate(new IVariable[]{DUMMY_VARIABLE, DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- BLOCK -----------------------------------
     */

    @IntegrationTest
    public void testItemStackBlock() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_BLOCK.evaluate(new IVariable[]{iStone});
        Asserts.check(res1 instanceof ValueObjectTypeBlock.ValueBlock, "result is a block");
        TestHelpers.assertEqual(((ValueObjectTypeBlock.ValueBlock) res1).getRawValue().get(), Blocks.stone.getDefaultState(), "block(stone) = stone");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputBlockBlockLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_BLOCK.evaluate(new IVariable[]{iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputBlockBlockSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_BLOCK.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeBlock() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_BLOCK.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- ISFLUIDSTACK -----------------------------------
     */

    @IntegrationTest
    public void testItemStackIsFluidStack() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_ISFLUIDSTACK.evaluate(new IVariable[]{iHoe});
        Asserts.check(res1 instanceof ValueTypeBoolean.ValueBoolean, "result is a boolean");
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), false, "isfluidstack(hoe) = false");

        IValue res2 = Operators.OBJECT_ITEMSTACK_ISFLUIDSTACK.evaluate(new IVariable[]{iBucketLava});
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res2).getRawValue(), true, "isfluidstack(bucketlava) = true");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputIsFluidStackIsFluidStackLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISFLUIDSTACK.evaluate(new IVariable[]{iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputIsFluidStackIsFluidStackSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISFLUIDSTACK.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeIsFluidStack() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISFLUIDSTACK.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- FLUIDSTACK -----------------------------------
     */

    @IntegrationTest
    public void testItemStackFluidStack() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_FLUIDSTACK.evaluate(new IVariable[]{iHoe});
        Asserts.check(res1 instanceof ValueObjectTypeFluidStack.ValueFluidStack, "result is a fluidstack");
        TestHelpers.assertEqual(((ValueObjectTypeFluidStack.ValueFluidStack) res1).getRawValue().isPresent(), false, "fluidstack(hoe) = null");

        IValue res2 = Operators.OBJECT_ITEMSTACK_FLUIDSTACK.evaluate(new IVariable[]{iBucketLava});
        TestHelpers.assertEqual(((ValueObjectTypeFluidStack.ValueFluidStack) res2).getRawValue().get().isFluidStackIdentical(new FluidStack(FluidRegistry.LAVA, FluidContainerRegistry.BUCKET_VOLUME)), true, "fluidstack(bucketlava) = lava:1000");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputFluidStackFluidStackLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_FLUIDSTACK.evaluate(new IVariable[]{iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputFluidStackFluidStackSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_FLUIDSTACK.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeFluidStack() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_FLUIDSTACK.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- FLUIDSTACK_CAPACITY -----------------------------------
     */

    @IntegrationTest
    public void testItemStackFluidStackCapacity() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_FLUIDSTACKCAPACITY.evaluate(new IVariable[]{iHoe});
        Asserts.check(res1 instanceof ValueTypeInteger.ValueInteger, "result is a fluidstack");
        TestHelpers.assertEqual(((ValueTypeInteger.ValueInteger) res1).getRawValue(), 0, "fluidstackcapacity(hoe) = 0");

        IValue res2 = Operators.OBJECT_ITEMSTACK_FLUIDSTACKCAPACITY.evaluate(new IVariable[]{iBucketLava});
        TestHelpers.assertEqual(((ValueTypeInteger.ValueInteger) res2).getRawValue(), FluidContainerRegistry.BUCKET_VOLUME, "fluidstackcapacity(bucketlava) = 1000");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputFluidStackCapacityFluidStackCapacityLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_FLUIDSTACKCAPACITY.evaluate(new IVariable[]{iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputFluidStackCapacityFluidStackCapacitySmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_FLUIDSTACKCAPACITY.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeFluidStackCapacity() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_FLUIDSTACKCAPACITY.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- ISNBTEQUAL -----------------------------------
     */

    @IntegrationTest
    public void testItemStackIsNBTEqual() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_ISNBTEQUAL.evaluate(new IVariable[]{iHoe, iPickaxe});
        Asserts.check(res1 instanceof ValueTypeBoolean.ValueBoolean, "result is a boolean");
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), false, "isnbtequal(hoe, pickaxe) = false");

        IValue res2 = Operators.OBJECT_ITEMSTACK_ISNBTEQUAL.evaluate(new IVariable[]{iHoe, iHoeEnchanted});
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res2).getRawValue(), false, "isnbtequal(hoe, hoeenchanted) = false");

        IValue res3 = Operators.OBJECT_ITEMSTACK_ISNBTEQUAL.evaluate(new IVariable[]{iPickaxe, iPickaxe});
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res3).getRawValue(), true, "isnbtequal(pickaxe, pickaxe) = true");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputIsNBTEqualIsNBTEqualLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISNBTEQUAL.evaluate(new IVariable[]{iApple, iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputIsNBTEqualIsNBTEqualSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISNBTEQUAL.evaluate(new IVariable[]{iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeIsNBTEqual() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISNBTEQUAL.evaluate(new IVariable[]{DUMMY_VARIABLE, DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- ISRAWITEMEQUAL -----------------------------------
     */

    @IntegrationTest
    public void testItemStackIsRawItemEqual() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_ISRAWITEMEQUAL.evaluate(new IVariable[]{iHoe, iPickaxe});
        Asserts.check(res1 instanceof ValueTypeBoolean.ValueBoolean, "result is a boolean");
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), false, "israwitemequal(hoe, pickaxe) = false");

        IValue res2 = Operators.OBJECT_ITEMSTACK_ISRAWITEMEQUAL.evaluate(new IVariable[]{iHoe, iHoeEnchanted});
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res2).getRawValue(), true, "israwitemequal(hoe, hoeenchanted) = true");

        IValue res3 = Operators.OBJECT_ITEMSTACK_ISRAWITEMEQUAL.evaluate(new IVariable[]{iPickaxe, iPickaxe});
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res3).getRawValue(), true, "israwitemequal(pickaxe, pickaxe) = true");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputIsRawItemEqualIsRawItemEqualLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISRAWITEMEQUAL.evaluate(new IVariable[]{iApple, iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputIsRawItemEqualIsRawItemEqualSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISRAWITEMEQUAL.evaluate(new IVariable[]{iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeIsRawItemEqual() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISRAWITEMEQUAL.evaluate(new IVariable[]{DUMMY_VARIABLE, DUMMY_VARIABLE});
    }

}