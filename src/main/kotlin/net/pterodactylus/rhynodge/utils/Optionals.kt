package net.pterodactylus.rhynodge.utils

import com.google.common.base.Optional

fun <T> T?.asOptional(): Optional<T> = Optional.fromNullable(this)
fun String?.asOptional(): Optional<String> = if ((this == null) || (this.isEmpty())) Optional.absent() else Optional.of(this)
