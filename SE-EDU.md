# SE-EDU
###### \java\seedu\address\logic\parser\EditCommandParserTest.java
``` java
        // tags
        userInput = targetIndex.getOneBased() + TAG_DESC_DIET_AMY;
        descriptor = new EditPersonDescriptorBuilder().withTags(VALID_TAG_DIET_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + PAYMENT_DESC_AMY + ATTENDANCE_DESC_AMY
                + TAG_DESC_DIET_AMY
                + PHONE_DESC_BOB
                + EMAIL_DESC_BOB + PAYMENT_DESC_BOB
                + TAG_DESC_DIET_BOB + ATTENDANCE_DESC_BOB;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB)
                .withPayment(VALID_PAYMENT_BOB)
                .withAttendance(VALID_ATTENDANCE_BOB)
                .withTags(VALID_TAG_DIET_BOB)
                .build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidValueFollowedByValidValue_success() {
        // no other valid values specified
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + INVALID_PHONE_DESC + PHONE_DESC_BOB;
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_BOB).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // other valid values specified
        userInput = targetIndex.getOneBased() + EMAIL_DESC_BOB + INVALID_PHONE_DESC + ATTENDANCE_DESC_BOB
                + PHONE_DESC_BOB + PAYMENT_DESC_BOB + TAG_DESC_DIET_BOB;
        descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withPayment(VALID_PAYMENT_BOB).withAttendance(VALID_ATTENDANCE_BOB)
                .withTags(VALID_TAG_DIET_BOB).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_resetTags_success() {
        Index targetIndex = INDEX_THIRD_PERSON;
        String userInput = targetIndex.getOneBased() + TAG_EMPTY;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withTags().build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
```
