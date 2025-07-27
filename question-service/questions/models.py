from django.db import models
from django.core.exceptions import ValidationError
import uuid
from django.db import models


class Question(models.Model):
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    text = models.TextField()
    image = models.ImageField(upload_to='question_images/', null=True, blank=True)
    created_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return self.text[:50]

class Option(models.Model):
    question = models.ForeignKey(Question, on_delete=models.CASCADE, related_name='options')
    text = models.CharField(max_length=255)
    is_correct = models.BooleanField()

    from django.core.exceptions import ValidationError

class Option(models.Model):
    question = models.ForeignKey(Question, on_delete=models.CASCADE, related_name='options')
    text = models.CharField(max_length=255)
    is_correct = models.BooleanField()

    def save(self, *args, **kwargs):
        if self.is_correct:
            existing = Option.objects.filter(question=self.question, is_correct=True)
            if self.pk:
                existing = existing.exclude(pk=self.pk)  # exclude self when updating
            if existing.exists():
                raise ValidationError("Only one correct option is allowed per question.")
        super().save(*args, **kwargs)

    def __str__(self):
        return f"{self.text} ({'Correct' if self.is_correct else 'Incorrect'})"


    def __str__(self):
        return f"{self.text} ({'Correct' if self.is_correct else 'Incorrect'})"
    

