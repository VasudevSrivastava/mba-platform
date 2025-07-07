from rest_framework import serializers
from .models import Question, Option

class QuestionListSerializer(serializers.ModelSerializer):
    class Meta:
        model = Question
        fields = ['id', 'text', 'image']

class OptionSerializer(serializers.ModelSerializer):
    class Meta:
        model = Option
        fields = ['text', 'is_correct']
    
class QuestionSerializer(serializers.ModelSerializer):
    options = OptionSerializer(many=True, read_only = True)
    class Meta:
        model = Question
        fields = ['id', 'text', 'image', 'options']
