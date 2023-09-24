package com.github.alexthe666.iceandfire.datagen.util;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.tags.TagKey;

import java.util.List;
import java.util.Optional;

public class ListHolderSet<T> extends HolderSet.ListBacked<T> {
    List<Holder<T>> contents;
    public ListHolderSet(List<Holder<T>> contents) {
        this.contents = contents;
    }
    @Override
    protected List<Holder<T>> contents() {
        return this.contents;
    }

    @Override
    public Either<TagKey<T>, List<Holder<T>>> unwrap() {
        return Either.right(this.contents);
    }

    @Override
    public boolean contains(Holder<T> pHolder) {
        return contents.contains(pHolder);
    }

    @Override
    public Optional<TagKey<T>> unwrapKey() {
        return Optional.empty();
    }
}
