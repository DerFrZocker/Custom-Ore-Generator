package de.derfrzocker.custom.ore.generator.impl;

import de.derfrzocker.custom.ore.generator.api.*;
import de.derfrzocker.spigot.utils.Config;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.ServicesManager;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("ConstantConditions")
@RunWith(PowerMockRunner.class)
public class OreConfigYamlImplTest { //TODO use a standard name convention / method by methods

    private final static Set<CustomData> testCustomData = new LinkedHashSet<>();
    private static CustomOreGeneratorService service;
    private final Random random = new Random(167324678234L);

    @BeforeClass
    public static void setUp() {
        ConfigurationSerialization.registerClass(OreConfigYamlImpl.class);

        final Server server;

        // mock Server
        {
            server = mock(Server.class);
            when(server.getLogger()).thenReturn(mock(Logger.class));
            when(server.getName()).thenReturn("spigot");
            Bukkit.setServer(server);
        }

        // mock CustomOreGeneratorService
        service = mock(CustomOreGeneratorService.class);

        final ServicesManager servicesManager = mock(ServicesManager.class);

        when(servicesManager.load(CustomOreGeneratorService.class)).thenReturn(service);
        when(server.getServicesManager()).thenReturn(servicesManager);

        final BlockSelector blockSelector = mock(BlockSelector.class);
        when(blockSelector.getName()).thenReturn("COUNT_RANGE");
        when(service.getDefaultBlockSelector()).thenReturn(blockSelector);
        when(service.getLogger()).thenReturn(mock(Logger.class));

        // Create some dummy CustomData for testing
        for (int i = 0; i < 10; i++) {
            final CustomData customData = new TestCustomData(String.valueOf(i));
            testCustomData.add(customData);
            when(service.getCustomData(customData.getName())).thenReturn(Optional.of(customData));
        }

    }

    @Test
    public void Create_When_OneArgumentIsNull_Expect_ThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new OreConfigYamlImpl("some-name", Material.PLAYER_HEAD, "some-ore-generator", null));
        assertThrows(IllegalArgumentException.class, () -> new OreConfigYamlImpl("some-name", Material.PLAYER_HEAD, null, "some-block-selector"));
        assertThrows(IllegalArgumentException.class, () -> new OreConfigYamlImpl("some-name", null, "some-ore-generator", "some-block-selector"));
        assertThrows(IllegalArgumentException.class, () -> new OreConfigYamlImpl(null, Material.PLAYER_HEAD, "some-ore-generator", "some-block-selector"));
    }

    @Test
    public void Create_When_NameIsEmpty_Expect_ThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new OreConfigYamlImpl("", Material.PLAYER_HEAD, "some-ore-generator", "some-block-selector"));
    }

    @Test
    public void GetName_Return_Given_Name() {
        final String name = "some-name";

        final OreConfigYamlImpl oreConfigYaml = new OreConfigYamlImpl(name, Material.PLAYER_HEAD, "some-ore-generator", "some-block-selector");

        assertEquals(name, oreConfigYaml.getName());
    }

    @Test
    public void TEST_Activate() {
        final OreConfigYamlImpl oreConfigYaml = new OreConfigYamlImpl("some-name", Material.PLAYER_HEAD, "some-ore-generator", "some-block-selector");

        for (int i = 0; i < 10; i++) {
            boolean status = false;
            if (random.nextInt(2) == 0)
                status = true;

            oreConfigYaml.setActivated(status);
            assertEquals(status, oreConfigYaml.isActivated());
        }
    }

    @Test
    public void TEST_GeneratedAll() {
        final OreConfigYamlImpl oreConfigYaml = new OreConfigYamlImpl("some-name", Material.PLAYER_HEAD, "some-ore-generator", "some-block-selector");

        for (int i = 0; i < 10; i++) {
            boolean status = false;
            if (random.nextInt(2) == 0)
                status = true;

            oreConfigYaml.setGeneratedAll(status);
            assertEquals(status, oreConfigYaml.shouldGeneratedAll());
        }
    }

    @Test
    public void GetBiome_When_Method_Call_Expect_ReturnNewSet() {
        final OreConfigYamlImpl oreConfigYaml = new OreConfigYamlImpl("some-name", Material.PLAYER_HEAD, "some-ore-generator", "some-block-selector");

        assertNotSame(oreConfigYaml.getBiomes(), oreConfigYaml.getBiomes());
    }

    @Test
    public void AddBiome_When_ArgumentIsNull_Expect_ThrowIllegalArgumentException() {
        final OreConfigYamlImpl oreConfigYaml = new OreConfigYamlImpl("some-name", Material.PLAYER_HEAD, "some-ore-generator", "some-block-selector");

        assertThrows(IllegalArgumentException.class, () -> oreConfigYaml.addBiome(null));
    }

    @Test
    public void AddBiome_When_ArgumentNotNull_Expect_AddToBiomeList() {
        final OreConfigYamlImpl oreConfigYaml = new OreConfigYamlImpl("some-name", Material.PLAYER_HEAD, "some-ore-generator", "some-block-selector");

        final Set<Biome> biomes = new HashSet<>();

        for (final Biome biome : Biome.values()) {
            biomes.add(biome);
            oreConfigYaml.addBiome(biome);

            assertEquals(biomes, oreConfigYaml.getBiomes());
        }
    }

    @Test
    public void RemoveBiome_When_ArgumentIsNull_Expect_ThrowIllegalArgumentException() {
        final OreConfigYamlImpl oreConfigYaml = new OreConfigYamlImpl("some-name", Material.PLAYER_HEAD, "some-ore-generator", "some-block-selector");

        assertThrows(IllegalArgumentException.class, () -> oreConfigYaml.removeBiome(null));
    }

    @Test
    public void RemoveBiome_When_ArgumentNotNull_Expect_RemoveFromBiomeList() {
        final OreConfigYamlImpl oreConfigYaml = new OreConfigYamlImpl("some-name", Material.PLAYER_HEAD, "some-ore-generator", "some-block-selector");

        final Set<Biome> biomes = new HashSet<>();

        for (final Biome biome : Biome.values()) {
            biomes.add(biome);
            oreConfigYaml.addBiome(biome);
        }

        for (final Biome biome : Biome.values()) {
            biomes.remove(biome);
            oreConfigYaml.removeBiome(biome);

            assertEquals(biomes, oreConfigYaml.getBiomes());
        }
    }

    @Test
    public void OreGeneratorTest() {
        final String oreGenerator = "some-ore-generator";

        final OreConfigYamlImpl oreConfigYaml = new OreConfigYamlImpl("some-name", Material.PLAYER_HEAD, oreGenerator, "some-block-selector");

        assertEquals(oreGenerator, oreConfigYaml.getOreGenerator());

        final String otherOreGeneratorName = "some-other-ore-generator";

        final OreGenerator otherOreGenerator = mock(OreGenerator.class);

        when(otherOreGenerator.getName()).thenReturn(otherOreGeneratorName);

        oreConfigYaml.setOreGenerator(otherOreGenerator);

        assertEquals(otherOreGeneratorName, oreConfigYaml.getOreGenerator());
    }

    @Test
    public void SetOreGenerator_When_ArgumentIsNull_Expect_ThrowIllegalArgumentException() {
        final OreConfigYamlImpl oreConfigYaml = new OreConfigYamlImpl("some-name", Material.PLAYER_HEAD, "some-ore-generator", "some-block-selector");

        assertThrows(IllegalArgumentException.class, () -> oreConfigYaml.setOreGenerator(null));
    }

    @Test
    public void BlockSelectorTest() {
        final String blockSelector = "some-block-selector";

        final OreConfigYamlImpl oreConfigYaml = new OreConfigYamlImpl("some-name", Material.PLAYER_HEAD, blockSelector, "some-block-selector");

        assertEquals(blockSelector, oreConfigYaml.getBlockSelector());

        final String otherBlockSelectorName = "some-other-block-selector";

        final BlockSelector otherBlockSelector = mock(BlockSelector.class);

        when(otherBlockSelector.getName()).thenReturn(otherBlockSelectorName);

        oreConfigYaml.setBlockSelector(otherBlockSelector);

        assertEquals(otherBlockSelectorName, oreConfigYaml.getBlockSelector());
    }

    @Test
    public void SetBlockSelector_When_ArgumentIsNull_Expect_ThrowIllegalArgumentException() {
        final OreConfigYamlImpl oreConfigYaml = new OreConfigYamlImpl("some-name", Material.PLAYER_HEAD, "some-ore-generator", "some-block-selector");

        assertThrows(IllegalArgumentException.class, () -> oreConfigYaml.setBlockSelector(null));
    }

    @Test
    public void GetValue_When_ArgumentIsNull_Expect_ThrowIllegalArgumentException() {
        final OreConfigYamlImpl oreConfigYaml = new OreConfigYamlImpl("some-name", Material.PLAYER_HEAD, "some-ore-generator", "some-block-selector");

        assertThrows(IllegalArgumentException.class, () -> oreConfigYaml.getValue(null));
    }

    @Test
    public void SetValue_When_ArgumentIsNull_Expect_ThrowIllegalArgumentException() {
        final OreConfigYamlImpl oreConfigYaml = new OreConfigYamlImpl("some-name", Material.PLAYER_HEAD, "some-ore-generator", "some-block-selector");

        assertThrows(IllegalArgumentException.class, () -> oreConfigYaml.setValue(null, -1));
    }

    @Test
    public void GetValue_When_OreConfigDontHaveOreSetting_Expect_ReturnEmptyOptional() {
        final OreConfigYamlImpl oreConfigYaml = new OreConfigYamlImpl("some-name", Material.PLAYER_HEAD, "some-ore-generator", "some-block-selector");

        final Optional<Integer> optionalInteger = oreConfigYaml.getValue(OreSettings.HEIGHT_RANGE);

        assertNotNull(optionalInteger);
        assertFalse(optionalInteger.isPresent());
    }

    @Test
    public void GetValueAndSetValue_When_OreConfigHaveOreSetting_Expect_ReturnRightValue() {
        final OreConfigYamlImpl oreConfigYaml = new OreConfigYamlImpl("some-name", Material.PLAYER_HEAD, "some-ore-generator", "some-block-selector");

        for (final OreSetting oreSetting : OreSetting.getOreSettings()) {
            final int value = random.nextInt(100);

            oreConfigYaml.setValue(oreSetting, value);

            final Optional<Integer> optionalInteger = oreConfigYaml.getValue(oreSetting);

            assertNotNull(optionalInteger);
            assertTrue(optionalInteger.isPresent());
            assertEquals(value, (int) optionalInteger.get());
        }
    }

    @Test
    public void SetValue_When_OreConfigHaveOreSetting_Expect_OverrideWithNewValues() {
        final OreConfigYamlImpl oreConfigYaml = new OreConfigYamlImpl("some-name", Material.PLAYER_HEAD, "some-ore-generator", "some-block-selector");

        //fill values
        for (final OreSetting oreSetting : OreSetting.getOreSettings()) {
            final int value = random.nextInt(100) + 150; // add 150 to be 100% sure they are not set the same value twice

            oreConfigYaml.setValue(oreSetting, value);
        }

        //replace and check
        for (final OreSetting oreSetting : OreSetting.getOreSettings()) {
            final int value = random.nextInt(100);

            oreConfigYaml.setValue(oreSetting, value);

            final Optional<Integer> optionalInteger = oreConfigYaml.getValue(oreSetting);

            assertNotNull(optionalInteger);
            assertTrue(optionalInteger.isPresent());
            assertEquals(value, (int) optionalInteger.get());
        }
    }

    @Test
    public void RemoveValue_When_NoValueIsSet_Expect_ReturnFalse() {
        final OreConfigYamlImpl oreConfigYaml = new OreConfigYamlImpl("some-name", Material.PLAYER_HEAD, "some-ore-generator", "some-block-selector");


        for (final OreSetting oreSetting : OreSetting.getOreSettings()) {
            assertFalse(oreConfigYaml.removeValue(oreSetting));
        }
    }

    @Test
    public void RemoveValue_When_ValueIsSet_Expect_ReturnTrueAndRemoveValue() {
        final OreConfigYamlImpl oreConfigYaml = new OreConfigYamlImpl("some-name", Material.PLAYER_HEAD, "some-ore-generator", "some-block-selector");

        for (final OreSetting oreSetting : OreSetting.getOreSettings()) {
            final int value = random.nextInt(100); // add 150 to be 100% sure they are not set the same value twice

            oreConfigYaml.setValue(oreSetting, value);
        }

        for (final OreSetting oreSetting : OreSetting.getOreSettings()) {
            assertTrue(oreConfigYaml.removeValue(oreSetting));
            assertFalse(oreConfigYaml.getOreSettings().containsKey(oreSetting));
            assertFalse(oreConfigYaml.getValue(oreSetting).isPresent());
        }
    }

    @Test
    public void GetOreSettings_Expect_NotSameMap() {
        final OreConfigYamlImpl oreConfigYaml = new OreConfigYamlImpl("some-name", Material.PLAYER_HEAD, "some-ore-generator", "some-block-selector");

        assertNotSame(oreConfigYaml.getOreSettings(), oreConfigYaml.getOreSettings());
    }

    @Test
    public void GetCustomData_When_ArgumentIsNull_Expect_ThrowIllegalArgumentException() {
        final OreConfigYamlImpl oreConfigYaml = new OreConfigYamlImpl("some-name", Material.PLAYER_HEAD, "some-ore-generator", "some-block-selector");

        assertThrows(IllegalArgumentException.class, () -> oreConfigYaml.getCustomData(null));
    }

    @Test
    public void SetCustomData_When_FirstArgumentIsNull_Expect_ThrowIllegalArgumentException() {
        final OreConfigYamlImpl oreConfigYaml = new OreConfigYamlImpl("some-name", Material.PLAYER_HEAD, "some-ore-generator", "some-block-selector");

        assertThrows(IllegalArgumentException.class, () -> oreConfigYaml.setCustomData(null, -1));
    }

    @Test
    public void GetCustomData_When_OreConfigDontHaveCustomData_Expect_ReturnEmptyOptional() {
        final OreConfigYamlImpl oreConfigYaml = new OreConfigYamlImpl("some-name", Material.PLAYER_HEAD, "some-ore-generator", "some-block-selector");

        final Optional<Object> optionalObject = oreConfigYaml.getCustomData(testCustomData.iterator().next());

        assertNotNull(optionalObject);
        assertFalse(optionalObject.isPresent());
    }

    @Test
    public void GetCustomDataAndSetCustomData_When_OreConfigHaveCustomData_Expect_ReturnRightValue() {
        final OreConfigYamlImpl oreConfigYaml = new OreConfigYamlImpl("some-name", Material.PLAYER_HEAD, "some-ore-generator", "some-block-selector");

        for (final CustomData customData : testCustomData) {
            final int value = random.nextInt(100);

            oreConfigYaml.setCustomData(customData, String.valueOf(value));

            final Optional<Object> optionalObject = oreConfigYaml.getCustomData(customData);

            assertNotNull(optionalObject);
            assertTrue(optionalObject.isPresent());
            assertEquals(String.valueOf(value), optionalObject.get());
        }
    }

    @Test
    public void SetCustomData_When_OreConfigHaveCustomData_Expect_OverrideWithNewValues() {
        final OreConfigYamlImpl oreConfigYaml = new OreConfigYamlImpl("some-name", Material.PLAYER_HEAD, "some-ore-generator", "some-block-selector");

        //fill values
        for (final CustomData customData : testCustomData) {
            final int value = random.nextInt(100) + 150; // add 150 to be 100% sure they are not set the same value twice

            oreConfigYaml.setCustomData(customData, String.valueOf(value));
        }

        //replace and check
        for (final CustomData customData : testCustomData) {
            final int value = random.nextInt(100);

            oreConfigYaml.setCustomData(customData, String.valueOf(value));

            final Optional<Object> optionalObject = oreConfigYaml.getCustomData(customData);

            assertNotNull(optionalObject);
            assertTrue(optionalObject.isPresent());
            assertEquals(String.valueOf(value), optionalObject.get());
        }
    }

    @Test
    public void SetCustomData_When_ObjectIsNull_Expect_DoNothing() {
        final OreConfigYamlImpl oreConfigYaml = new OreConfigYamlImpl("some-name", Material.PLAYER_HEAD, "some-ore-generator", "some-block-selector");

        for (final CustomData customData : testCustomData) {

            oreConfigYaml.setCustomData(customData, null);

            final Optional<Object> optionalObject = oreConfigYaml.getCustomData(customData);

            assertNotNull(optionalObject);
            assertFalse(optionalObject.isPresent());
        }
    }

    @Test
    public void SetCustomData_When_ObjectIsNullAndOreConfigHaveCustomData_Expect_RemoveCustomDataValue() {
        final OreConfigYamlImpl oreConfigYaml = new OreConfigYamlImpl("some-name", Material.PLAYER_HEAD, "some-ore-generator", "some-block-selector");

        //fill values
        for (final CustomData customData : testCustomData) {
            final int value = random.nextInt(100) + 150; // add 150 to be 100% sure they are not set the same value twice

            oreConfigYaml.setCustomData(customData, String.valueOf(value));
        }

        for (final CustomData customData : testCustomData) {

            oreConfigYaml.setCustomData(customData, null);

            final Optional<Object> optionalObject = oreConfigYaml.getCustomData(customData);

            assertNotNull(optionalObject);
            assertFalse(optionalObject.isPresent());
        }
    }

    @Test
    public void GetCustomData_Expect_NotSameMap() {
        final OreConfigYamlImpl oreConfigYaml = new OreConfigYamlImpl("some-name", Material.PLAYER_HEAD, "some-ore-generator", "some-block-selector");

        assertNotSame(oreConfigYaml.getCustomData(), oreConfigYaml.getCustomData());
    }

    @Test
    public void Test_Deserialization_New_Format() {
        try (final InputStream inputStream = getClass().getResourceAsStream("/OreConfigYamlImpl_NewFormat.yml")) {
            OreSettings.HEIGHT_CENTER.getName();
            final YamlConfiguration configuration = new Config(inputStream);

            // test with existing OreSettings and CustomData
            {
                final Object object = configuration.get("ore-config");

                assertNotNull(object);
                assertTrue(object instanceof OreConfigYamlImpl);
                final OreConfigYamlImpl oreConfigYaml = (OreConfigYamlImpl) object;

                assertEquals("test", oreConfigYaml.getName());
                assertEquals(Material.EMERALD_BLOCK, oreConfigYaml.getMaterial());
                assertEquals("VANILLA_MINABLE_GENERATOR", oreConfigYaml.getOreGenerator());
                assertEquals("COUNT_RANGE", oreConfigYaml.getBlockSelector());
                assertTrue(oreConfigYaml.isActivated());
                assertFalse(oreConfigYaml.shouldGeneratedAll());
                final Set<Biome> biomes = oreConfigYaml.getBiomes();
                assertEquals(2, biomes.size());
                assertTrue(biomes.contains(Biome.FOREST));
                assertTrue(biomes.contains(Biome.DEEP_WARM_OCEAN));
                final Map<OreSetting, Integer> oreSettingIntegerMap = oreConfigYaml.getOreSettings();
                assertEquals(4, oreSettingIntegerMap.size());
                assertEquals(15, (int) oreSettingIntegerMap.get(OreSettings.VEINS_PER_CHUNK));
                assertEquals(10, (int) oreSettingIntegerMap.get(OreSettings.MINIMUM_HEIGHT));
                assertEquals(20, (int) oreSettingIntegerMap.get(OreSettings.HEIGHT_RANGE));
                assertEquals(5, (int) oreSettingIntegerMap.get(OreSettings.VEIN_SIZE));

                final Map<CustomData, Object> customDataObjectMap = oreConfigYaml.getCustomData();
                assertEquals(4, customDataObjectMap.size());
                final Iterator<CustomData> iterator = testCustomData.iterator();
                iterator.next(); // skip 0 CustomData
                assertEquals("Test_value_1", customDataObjectMap.get(iterator.next()));
                assertEquals("Test_value_2", customDataObjectMap.get(iterator.next()));
                assertEquals("Test_value_3", customDataObjectMap.get(iterator.next()));
                assertEquals("Test_value_4", customDataObjectMap.get(iterator.next()));
            }

            // test with existing OreSettings and CustomData and no block selector
            {
                final Object object = configuration.get("ore-config_3");

                assertNotNull(object);
                assertTrue(object instanceof OreConfigYamlImpl);
                final OreConfigYamlImpl oreConfigYaml = (OreConfigYamlImpl) object;

                assertEquals("test_3", oreConfigYaml.getName());
                assertEquals(Material.EMERALD_BLOCK, oreConfigYaml.getMaterial());
                assertEquals("VANILLA_MINABLE_GENERATOR", oreConfigYaml.getOreGenerator());
                assertEquals("COUNT_RANGE", oreConfigYaml.getBlockSelector());
                assertTrue(oreConfigYaml.isActivated());
                assertFalse(oreConfigYaml.shouldGeneratedAll());
                final Set<Biome> biomes = oreConfigYaml.getBiomes();
                assertEquals(2, biomes.size());
                assertTrue(biomes.contains(Biome.FOREST));
                assertTrue(biomes.contains(Biome.DEEP_WARM_OCEAN));
                final Map<OreSetting, Integer> oreSettingIntegerMap = oreConfigYaml.getOreSettings();
                assertEquals(4, oreSettingIntegerMap.size());
                assertEquals(15, (int) oreSettingIntegerMap.get(OreSettings.VEINS_PER_CHUNK));
                assertEquals(10, (int) oreSettingIntegerMap.get(OreSettings.MINIMUM_HEIGHT));
                assertEquals(20, (int) oreSettingIntegerMap.get(OreSettings.HEIGHT_RANGE));
                assertEquals(5, (int) oreSettingIntegerMap.get(OreSettings.VEIN_SIZE));

                final Map<CustomData, Object> customDataObjectMap = oreConfigYaml.getCustomData();
                assertEquals(4, customDataObjectMap.size());
                final Iterator<CustomData> iterator = testCustomData.iterator();
                iterator.next(); // skip 0 CustomData
                assertEquals("Test_value_1", customDataObjectMap.get(iterator.next()));
                assertEquals("Test_value_2", customDataObjectMap.get(iterator.next()));
                assertEquals("Test_value_3", customDataObjectMap.get(iterator.next()));
                assertEquals("Test_value_4", customDataObjectMap.get(iterator.next()));
            }

            // test with non existing OreSettings and CustomData
            {
                final Object object = configuration.get("ore-config_2");

                assertNotNull(object);
                assertTrue(object instanceof OreConfigYamlImpl);
                final OreConfigYamlImpl oreConfigYaml = (OreConfigYamlImpl) object;

                assertEquals("test2", oreConfigYaml.getName());
                assertEquals(Material.EMERALD_BLOCK, oreConfigYaml.getMaterial());
                assertEquals("VANILLA_MINABLE_GENERATOR_2", oreConfigYaml.getOreGenerator());
                assertEquals("COUNT_RANGE_2", oreConfigYaml.getBlockSelector());
                assertFalse(oreConfigYaml.isActivated());
                assertTrue(oreConfigYaml.shouldGeneratedAll());
                final Set<Biome> biomes = oreConfigYaml.getBiomes();
                assertEquals(0, biomes.size());
                final Map<OreSetting, Integer> oreSettingIntegerMap = oreConfigYaml.getOreSettings();
                assertEquals(0, oreSettingIntegerMap.size());
                final Map<CustomData, Object> customDataObjectMap = oreConfigYaml.getCustomData();
                assertEquals(0, customDataObjectMap.size());


                //register some of the non existing OreSettings and CustomData
                final OreSetting VEINS_PER_CHUNK_ = OreSetting.createOreSetting("VEINS_PER_CHUNK_", -1);
                final OreSetting MINIMUM_HEIGHT_ = OreSetting.createOreSetting("MINIMUM_HEIGHT_", -1);
                final CustomData customData1 = new TestCustomData("1_2");
                final CustomData customData2 = new TestCustomData("1_2");
                when(service.getCustomData("1_2")).thenReturn(Optional.of(customData1));
                when(service.getCustomData("2_2")).thenReturn(Optional.of(customData2));

                //test again
                {
                    final Map<OreSetting, Integer> oreSettingIntegerMap2 = oreConfigYaml.getOreSettings();
                    assertEquals(2, oreSettingIntegerMap2.size());
                    assertEquals(15, (int) oreSettingIntegerMap2.get(VEINS_PER_CHUNK_));
                    assertEquals(10, (int) oreSettingIntegerMap2.get(MINIMUM_HEIGHT_));
                    final Map<CustomData, Object> customDataObjectMap2 = oreConfigYaml.getCustomData();
                    assertEquals(2, customDataObjectMap2.size());
                    assertEquals("Test_value_1", customDataObjectMap2.get(customData1));
                    assertEquals("Test_value_2", customDataObjectMap2.get(customData2));
                }
            }
        } catch (final IOException e) {
            fail(e);
        }
    }

    @Test
    public void Test_Deserialization_Old_Format() {
        try (final InputStream inputStream = getClass().getResourceAsStream("/OreConfigYamlImpl_OldFormat.yml")) {
            OreSettings.HEIGHT_CENTER.getName();
            final YamlConfiguration configuration = new Config(inputStream);

            // test with existing OreSettings and CustomData
            {
                final Object object = configuration.get("ore-config");

                assertNotNull(object);
                assertTrue(object instanceof OreConfigYamlImpl);
                assertTrue(object instanceof DummyOreConfig);
                final OreConfigYamlImpl oreConfigYaml = (OreConfigYamlImpl) object;

                assertEquals("dummy_ore_config", oreConfigYaml.getName());
                assertEquals(Material.EMERALD_ORE, oreConfigYaml.getMaterial());
                assertEquals("VANILLA_MINABLE_GENERATOR", oreConfigYaml.getOreGenerator());
                assertEquals("COUNT_RANGE", oreConfigYaml.getBlockSelector());
                assertTrue(oreConfigYaml.isActivated());
                assertTrue(oreConfigYaml.shouldGeneratedAll());
                final Set<Biome> biomes = oreConfigYaml.getBiomes();
                assertEquals(0, biomes.size());
                final Map<OreSetting, Integer> oreSettingIntegerMap = oreConfigYaml.getOreSettings();
                assertEquals(4, oreSettingIntegerMap.size());
                assertEquals(15, (int) oreSettingIntegerMap.get(OreSettings.VEINS_PER_CHUNK));
                assertEquals(10, (int) oreSettingIntegerMap.get(OreSettings.MINIMUM_HEIGHT));
                assertEquals(20, (int) oreSettingIntegerMap.get(OreSettings.HEIGHT_RANGE));
                assertEquals(5, (int) oreSettingIntegerMap.get(OreSettings.VEIN_SIZE));
                final Map<CustomData, Object> customDataObjectMap = oreConfigYaml.getCustomData();
                assertEquals(0, customDataObjectMap.size());
            }

        } catch (final IOException e) {
            fail(e);
        }
    }

    @Test
    public void Test_Serialize() {
        final OreConfigYamlImpl oreConfigYaml = new OreConfigYamlImpl("some-name", Material.PLAYER_HEAD, "some-ore-generator", "some-block-selector");

        oreConfigYaml.setGeneratedAll(false);
        oreConfigYaml.setValue(OreSettings.HEIGHT_RANGE, 20);
        oreConfigYaml.setValue(OreSettings.MINIMUM_HEIGHT, 10);
        final Iterator<CustomData> customDataIterator = testCustomData.iterator();
        oreConfigYaml.setCustomData(customDataIterator.next(), "Test_value_1");
        oreConfigYaml.setCustomData(customDataIterator.next(), "Test_value_2");
        oreConfigYaml.addBiome(Biome.FOREST);
        oreConfigYaml.addBiome(Biome.FROZEN_OCEAN);

        final Map<String, Object> serialize = oreConfigYaml.serialize();

        assertEquals(9, serialize.size());
        final Object name = serialize.get("name");
        assertNotNull(name);
        assertTrue(name instanceof String);
        assertEquals("some-name", name);
        final Object material = serialize.get("material");
        assertNotNull(material);
        assertTrue(material instanceof String);
        assertEquals("PLAYER_HEAD", material);
        final Object oreGenerator = serialize.get("ore-generator");
        assertNotNull(oreGenerator);
        assertTrue(oreGenerator instanceof String);
        assertEquals("some-ore-generator", oreGenerator);
        final Object blockSelector = serialize.get("block-selector");
        assertNotNull(blockSelector);
        assertTrue(blockSelector instanceof String);
        assertEquals("some-block-selector", blockSelector);
        final Object activated = serialize.get("activated");
        assertNotNull(activated);
        assertTrue(activated instanceof Boolean);
        assertEquals(true, activated);
        final Object generatedAll = serialize.get("generated-all");
        assertNotNull(generatedAll);
        assertTrue(generatedAll instanceof Boolean);
        assertEquals(false, generatedAll);
        final Object biomes = serialize.get("biomes");
        assertNotNull(biomes);
        assertTrue(biomes instanceof List);
        final List<?> biomesList = (List<?>) biomes;
        assertEquals(2, biomesList.size());
        assertTrue(biomesList.contains("FOREST"));
        assertTrue(biomesList.contains("FROZEN_OCEAN"));
        final Object oreSettings = serialize.get("ore-settings");
        assertNotNull(oreSettings);
        assertTrue(oreSettings instanceof Map);
        final Map<?, ?> oreSettingsMap = (Map<?, ?>) oreSettings;
        assertEquals(2, oreSettingsMap.size());
        assertTrue(oreSettingsMap.containsKey("HEIGHT_RANGE"));
        assertEquals(20, oreSettingsMap.get("HEIGHT_RANGE"));
        assertTrue(oreSettingsMap.containsKey("MINIMUM_HEIGHT"));
        assertEquals(10, oreSettingsMap.get("MINIMUM_HEIGHT"));
        final Object customData = serialize.get("custom-data");
        assertNotNull(customData);
        assertTrue(customData instanceof Map);
        final Map<?, ?> customDataMap = (Map<?, ?>) customData;
        final Iterator<CustomData> customDataIterator1 = testCustomData.iterator();
        assertEquals(2, customDataMap.size());
        final String customData1Name = customDataIterator1.next().getName();
        assertTrue(customDataMap.containsKey(customData1Name));
        assertEquals("Test_value_1", customDataMap.get(customData1Name));
        final String customData2Name = customDataIterator1.next().getName();
        assertTrue(customDataMap.containsKey(customData2Name));
        assertEquals("Test_value_2", customDataMap.get(customData2Name));
    }

}
