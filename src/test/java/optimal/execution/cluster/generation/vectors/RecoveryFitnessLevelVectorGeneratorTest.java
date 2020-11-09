package optimal.execution.cluster.generation.vectors;

import optimal.configuration.ProbabilityVectorGenerationConfiguration;
import optimal.configuration.loaders.ProbabilityVectorGenerationConfigurationLoader;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import javax.naming.ConfigurationException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class RecoveryFitnessLevelVectorGeneratorTest {

    private static final String RESOURCES_PATH = "execution/cluster/generation/vectors/";

    private Path myJsonConfigsDir;
    private List<Path> myJsonFiles;
    private Path myVectorsDir;

    private static void deleteAfterFinish(Path path) {
        File directory = new File(path.toUri());
        directory.deleteOnExit();
    }

    private List<String> getResourceFiles(String path) throws IOException {
        List<String> filenames = new ArrayList<>();

        try (
                InputStream in = getResourceAsStream(path);
                BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String resource;

            while ((resource = br.readLine()) != null) {
                filenames.add(resource);
            }
        }
        return filenames;
    }


    private InputStream getResourceAsStream(String resource) {
        final InputStream in
                = getContextClassLoader().getResourceAsStream(resource);

        return in == null ? getClass().getResourceAsStream(resource) : in;
    }

    private ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    @BeforeEach
    void setUp(TestInfo testInfo) throws IOException, ConfigurationException {
        myJsonConfigsDir = Files.createTempDirectory("jsonConfig");
        deleteAfterFinish(myJsonConfigsDir);

        myVectorsDir = Files.createTempDirectory("vectors");
        deleteAfterFinish(myVectorsDir);

        final String displayName = testInfo.getDisplayName();
        final String configsDirName = displayName.substring(displayName.indexOf('$') + 1, displayName.indexOf('('));
        final String jsonConfigsDir = RESOURCES_PATH + configsDirName + "/jsonConfigs";
        myJsonFiles = new ArrayList<>();
        for (String file : getResourceFiles(jsonConfigsDir)) {
            String resourceFile = jsonConfigsDir + "/" + file;
            final ProbabilityVectorGenerationConfigurationLoader loader =
                    new ProbabilityVectorGenerationConfigurationLoader(resourceFile);
            final ProbabilityVectorGenerationConfiguration c =
                    loader.getConfigurationFromResources();
            final String outputDirectory = c.getOutputDirectory();
            final String newOutputDirectory = outputDirectory.replace("$#$",
                    myVectorsDir.toAbsolutePath().toString());
            final ProbabilityVectorGenerationConfiguration newConfiguration =
                    new ProbabilityVectorGenerationConfiguration(
                            c.getProbability(), c.getFitness(), c.getProblemConfig(), c.getAlgorithmConfig(),
                            c.getStopConditionConfig(), c.getOutputFileName(), newOutputDirectory);
            final String serializeConfiguration = loader.serializeConfiguration(newConfiguration);
            final String jsonInTempDirFileName = myJsonConfigsDir + "/" + file;
            final Path pathToJsonInTmpDir = Paths.get(jsonInTempDirFileName);
            deleteAfterFinish(pathToJsonInTmpDir);
            myJsonFiles.add(pathToJsonInTmpDir);
            final BufferedWriter writer = new BufferedWriter(new FileWriter(jsonInTempDirFileName));
            writer.write(serializeConfiguration);
            writer.close();
        }
        final String vectorsConfigDir = RESOURCES_PATH + configsDirName;
        for (String directory : getResourceFiles(vectorsConfigDir)) {
            if (!directory.startsWith("vectors")) {
                continue;
            }
            final String currentDirectory = vectorsConfigDir + "/" + directory;
            final String tmpDirectory = myVectorsDir + "/" + directory;
            Files.createDirectories(Paths.get(tmpDirectory));
            deleteAfterFinish(Paths.get(tmpDirectory));
            for (String csvFile : getResourceFiles(currentDirectory)) {
                final String csvFileFqn = currentDirectory + "/" + csvFile;
                final String copyCsvFileName = tmpDirectory + "/" + csvFile;
                final InputStream inputStream = getResourceAsStream(csvFileFqn);
                final Path target = Paths.get(copyCsvFileName);
                Files.copy(
                        inputStream,
                        target,
                        StandardCopyOption.REPLACE_EXISTING);
                inputStream.close();
                deleteAfterFinish(target);
            }
        }
    }
    
    private static boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    @AfterEach
    void tearDown() {
        myJsonFiles = null;
        if (Files.exists(myVectorsDir)) {
            deleteDirectory(myVectorsDir.toFile());
        }
        if (Files.exists(myJsonConfigsDir)) {
            deleteDirectory(myJsonConfigsDir.toFile());
        }
        myJsonConfigsDir = null;
        myVectorsDir = null;
    }

    @Test
    public void testRecoveringOfBadFile$config1() {
        final AtomicInteger atomicInteger = new AtomicInteger();
        iterateJsonConfigsWithRecovering(atomicInteger);
        Assertions.assertEquals(1, atomicInteger.get());
    }

    @Test
    public void testRecoveringOfBadFileWhenGoodIsThere$config2() {
        final AtomicInteger atomicInteger = new AtomicInteger();
        iterateJsonConfigsWithRecovering(atomicInteger);
        Assertions.assertEquals(1, atomicInteger.get());
    }

    @Test
    public void testRecoveringOfSkippedFile$config3() {
        final AtomicInteger atomicInteger = new AtomicInteger();
        iterateJsonConfigsWithRecovering(atomicInteger);
        Assertions.assertEquals(1, atomicInteger.get());
    }

    @Test
    public void testSkippingOfGoodFiles$config4() {
        final AtomicInteger atomicInteger = new AtomicInteger();
        iterateJsonConfigsWithRecovering(atomicInteger);
        Assertions.assertEquals(0, atomicInteger.get());
    }

    @Test
    public void testRecoveryOfEmptyFile$config5() {
        final AtomicInteger atomicInteger = new AtomicInteger();
        iterateJsonConfigsWithRecovering(atomicInteger);
        Assertions.assertEquals(1, atomicInteger.get());
    }

    private void iterateJsonConfigsWithRecovering(AtomicInteger atomicInteger) {
        for (Path configFilePath : myJsonFiles) {
            final FitnessLevelVectorGenerator fitnessLevelVectorGenerator =
                    FitnessLevelVectorGenerator.createFitnessLevelVectorGenerator(configFilePath.toAbsolutePath().toString(),
                            "",
                            FitnessLevelVectorGenerator.Strategy.RECOVERY);
            final FitnessLevelVectorGenerator spyVectorGenerator = Mockito.spy(fitnessLevelVectorGenerator);
            Mockito.doAnswer(i -> {
                atomicInteger.incrementAndGet();
                return new ArrayList<>();
            }).when(spyVectorGenerator).runVectorGeneration();
            spyVectorGenerator.generateFitnessIncreasesVector();
        }
    }

}