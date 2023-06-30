package com.tterrag.registrate.providers;

import com.tterrag.registrate.AbstractRegistrate;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;

import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.data.ExistingFileHelper;

public class RegistrateTagsProvider<T> extends TagsProvider<T> implements RegistrateProvider {

public interface RegistrateTagsProvider<T> extends RegistrateProvider {
    TagsProvider.TagAppender<T> addTag(TagKey<T> tag);

    class Impl<T> extends TagsProvider<T> implements RegistrateTagsProvider<T> {
        private final AbstractRegistrate<?> owner;
        private final ProviderType<? extends Impl<T>> type;
        private final String name;

        public Impl(AbstractRegistrate<?> owner, ProviderType<? extends Impl<T>> type, String name, PackOutput packOutput, ResourceKey<? extends Registry<T>> registryIn, CompletableFuture<HolderLookup.Provider> registriesLookup, ExistingFileHelper existingFileHelper) {
            super(packOutput, registryIn, registriesLookup, owner.getModid(), existingFileHelper);

            this.owner = owner;
            this.type = type;
            this.name = name;
        }

        @Override
        public String getName() {
            return "Tags (%s)".formatted(name);
        }

        @Override
        protected void generateTags(HolderLookup.Provider provider) {
            owner.genData(type, this);
        }

        @Override
        public EnvType getSide() {
            return EnvType.SERVER;
        }

        @Override
        public FabricTagBuilder<T> addTag(TagKey<T> tag) {
            return super.getOrCreateTagBuilder(tag);
        }
    }

    class IntrinsicImpl<T> extends IntrinsicHolderTagsProvider<T> implements RegistrateTagsProvider<T> {
        private final AbstractRegistrate<?> owner;
        private final ProviderType<? extends IntrinsicImpl<T>> type;
        private final String name;

        public IntrinsicImpl(AbstractRegistrate<?> owner, ProviderType<? extends IntrinsicImpl<T>> type, String name, PackOutput packOutput, ResourceKey<? extends Registry<T>> registryIn, CompletableFuture<HolderLookup.Provider> registriesLookup, Function<T, ResourceKey<T>> keyExtractor, ExistingFileHelper existingFileHelper) {
            super(packOutput, registryIn, registriesLookup, keyExtractor, owner.getModid(), existingFileHelper);

            this.owner = owner;
            this.type = type;
            this.name = name;
        }

        @Override
        public String getName() {
            return "Tags (%s)".formatted(name);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            owner.genData(type, this);
        }

        @Override
        public LogicalSide getSide() {
            return LogicalSide.SERVER;
        }

        @Override
        public IntrinsicTagAppender<T> addTag(TagKey<T> tag) {
            return super.tag(tag);
        }
    }
}
