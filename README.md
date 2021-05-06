# ea

This fork of repository contains implementation of method for numerical computation of mutation rates which minimize the expected runtime of (1+lambda) EA.

- The implementation for general case of fitness function is located in class `src/main/java/optimal/optimal2/BestMutationRateSearcher2.java`
- Code to create heatmaps and process data to plot other charts is located in package `src/main/java/pictures`

We also support distributed computing of transition probabilities which are used in the method. Those probabilities may be obtained in the runtime of our method and precomputed on the cluster based on configuration.

- Implementation of different configurations is located in `src/main/java/optimal/configuration`
- Main class to run computation in cluster is located in class `src/main/java/optimal/InClusterExperiments.java` 
