from django.contrib import admin
from .models import Question, Option

class OptionInline(admin.StackedInline):
    model = Option
    extra = 4


@admin.register(Question)
class QuestionAdmin(admin.ModelAdmin):
    inlines = [OptionInline]