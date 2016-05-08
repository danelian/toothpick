package toothpick.registries;

import toothpick.Factory;

import static java.lang.String.format;

/**
 * Locates the {@link FactoryRegistry} instances.
 * The registries form a tree, or a forest (collection of disjoint trees).
 * Each registry knows of to find the registries it depends on (called children registries).
 * When compiling a library, developers must pass to the annotation compiler the name of the children registries
 * used by this library (i.e. the name of the registries of those libraries) and also provide the package
 * name of the registry that will be generated for the library being compiled.
 *
 * The generated registry will know about its children. An application only has to list here the high level
 * registries of the libraries that it uses. Their children libraries (and the tree they form with their own children
 * libraries, etc..), will be explored by the high level root library that uses them. The application should declare
 * root registries to the locator prior to performing any operation.
 *
 * In case no generated factory for a given class, we throw a {@link RuntimeException}.
 *
 * The locator acts as  facade for all the {@link FactoryRegistry} instances in the forest to retrieve a {@link Factory}
 * for a given class.
 *
 * @see FactoryRegistry
 * @see Factory
 */
public class FactoryRegistryLocator {
  private FactoryRegistryLocator() {
  }

  private static FactoryRegistry registry;

  public static void setRootRegistry(FactoryRegistry registry) {
    FactoryRegistryLocator.registry = registry;
  }

  public static <T> Factory<T> getFactory(Class<T> clazz) {
    Factory<T> factory;
    if (registry != null) {
      factory = registry.getFactory(clazz);
      if (factory != null) {
        return factory;
      }
    }
    throw new RuntimeException(format("No factory could be found for class %s." //
        + " Check that registries are properly setup with annotation processor arguments, " //
        + "or use annotations correctly in this class.", clazz.getName()));
  }
}