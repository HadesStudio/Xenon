package net.ascend.lib.menu.fill;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.lib.menu.fill
//   Date: Saturday, September 16, 2023 - 1:05 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.lib.menu.fill.types.BorderFiller;
import net.ascend.lib.menu.fill.types.FillFiller;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FillerTemplate {

    FILL(new FillFiller()),
    BORDER(new BorderFiller());

    private final MenuFiller menuFiller;

}
