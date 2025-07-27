from django.contrib import admin
from .models import Question, Option

from django.forms.models import BaseInlineFormSet
from django.core.exceptions import ValidationError

class OptionInlineFormSet(BaseInlineFormSet):
    def clean(self):
        super().clean()
        correct_count = 0
        for form in self.forms:
            if not form.cleaned_data.get('DELETE', False):  # ignore deleted forms
                if form.cleaned_data.get('is_correct'):
                    correct_count += 1
        if correct_count > 1:
            raise ValidationError("Only one option can be marked as correct.")
        if correct_count == 0:
            raise ValidationError("One option must be marked as correct.")


class OptionInline(admin.StackedInline):
    model = Option
    extra = 4
    formset = OptionInlineFormSet

@admin.register(Question)
class QuestionAdmin(admin.ModelAdmin):
    inlines = [OptionInline]