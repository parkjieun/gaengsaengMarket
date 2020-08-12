from konlpy.tag import Hannanum
import os
import json
import pandas as pd

class TitleAnalyzer:

    def __init__(self):
        self.hannanum = Hannanum()
        self.title = ""
        item_dictionary_name = 'dict/ItemDictionary'
        category_dictionary_name = 'dict/CategoryDictionary'
        hashtags_name = 'dict/Hashtags'
        if os.path.exists(item_dictionary_name):
            self.item_dictionary = json.load(open(item_dictionary_name,encoding="utf-8"))

        if os.path.exists(category_dictionary_name):
            self.category_dictionary = json.load(open(category_dictionary_name,encoding="utf-8"))

        if os.path.exists(hashtags_name):
            self.hashtags_dictionary = pd.read_csv(hashtags_name)

    def get_nouns(self, title):
        hashtags = []
        for noun in self.hannanum.nouns(title):
            if noun in self.hashtags_dictionary:
                hashtags.append(noun)
        return hashtags

    def get_category(self, title):
        for noun in self.hannanum.nouns(title.lower()):
            itemcode = self.item_dictionary.get(noun)
            if itemcode:
                category_data = self.category_dictionary.get(itemcode)
                return category_data

    def get_price(self,title):
        for noun in self.hannanum.nouns(title):
            if "원" in noun:
                return noun[:-1]




